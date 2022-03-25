package main

import (
	"encoding/json"
	"github.com/aws/aws-lambda-go/events"
	"github.com/smarkwal/jarhc-online/sam-app/common/maven"
	"log"
	"net/http"
	"strings"
)

type JapiccCheckRequest struct {
	OldVersion string `json:"oldVersion"`
	NewVersion string `json:"newVersion"`
}

type JapiccCheckMessage struct {
	OldVersion     string `json:"oldVersion"`
	NewVersion     string `json:"newVersion"`
	ReportFileName string `json:"reportFileName"`
}

func handleJapiccSubmit(request events.APIGatewayProxyRequest) (events.APIGatewayProxyResponse, error) {

	// get message from request body
	message := request.Body
	log.Println("message: ", message)

	// parse JSON message
	var in JapiccCheckRequest
	reader := strings.NewReader(message)
	err := json.NewDecoder(reader).Decode(&in)
	if err != nil {
		log.Println("error parsing JSON message:", err)
		return sendError(http.StatusBadRequest, err)
	}

	oldVersion := in.OldVersion
	log.Println("old version:", oldVersion)

	// check if old version is valid
	if !isValidVersion(oldVersion) {
		log.Println("version is not valid:", oldVersion)
		return sendErrorMessage(http.StatusBadRequest, "Version is not valid: "+oldVersion)
	}

	newVersion := in.NewVersion
	log.Println("new version:", newVersion)

	// check if new version is valid
	if !isValidVersion(newVersion) {
		log.Println("version is not valid:", newVersion)
		return sendErrorMessage(http.StatusBadRequest, "Version is not valid: "+newVersion)
	}

	if oldVersion == newVersion {
		log.Println("submitted same version twice:", oldVersion)
		return sendErrorMessage(http.StatusBadRequest, "Cannot compare version with itself: "+oldVersion)
	}

	// calculate hash for combination of versions
	config := oldVersion + "/" + newVersion
	hash := sha256hex(config)

	// prepare report file
	reportFileName := "report-" + hash + ".html"
	log.Println("report file name:", reportFileName)

	// check if report file already exists
	exists, err := s3Client.Exists(reportFileName)
	if err != nil {
		log.Println("error testing if report file exists in S3 bucket:", err)
		return sendError(http.StatusInternalServerError, err)
	}

	reportFileURL := s3Client.GetURL(reportFileName)
	if exists {
		log.Println("report file found in S3:", reportFileURL)
		return sendReportFile(reportFileURL)
	}

	// check if old version exists in Maven Central
	oldArtifact, _ := maven.NewArtifact(oldVersion)
	exists, _ = oldArtifact.Exists()
	if !exists {
		log.Println("version not found in Maven Central:", oldVersion)
		return sendErrorMessage(http.StatusBadRequest, "Version not found in Maven Central: "+oldVersion)
	}

	// check if new version exists in Maven Central
	newArtifact, _ := maven.NewArtifact(newVersion)
	exists, _ = newArtifact.Exists()
	if !exists {
		log.Println("version not found in Maven Central:", newVersion)
		return sendErrorMessage(http.StatusBadRequest, "Version not found in Maven Central: "+newVersion)
	}

	payload := JapiccCheckMessage{
		OldVersion:     oldVersion,
		NewVersion:     newVersion,
		ReportFileName: reportFileName,
	}

	err = lambdaClient.InvokeAsync("japicc-check", payload)
	if err != nil {
		log.Println("error invoking Lambda function:", err)
		return sendError(http.StatusInternalServerError, err)
	}

	return sendReportFile(reportFileURL)
}
