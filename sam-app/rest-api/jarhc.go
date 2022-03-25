package main

import (
	"encoding/json"
	"github.com/aws/aws-lambda-go/events"
	"github.com/smarkwal/jarhc-online/sam-app/common/maven"
	"log"
	"net/http"
	"strings"
)

type JarhcCheckRequest struct {
	Classpath []string `json:"classpath"`
	Provided  []string `json:"provided"`
}

type JarhcCheckMessage struct {
	Classpath      []string `json:"classpath"`
	Provided       []string `json:"provided"`
	ReportFileName string   `json:"reportFileName"`
}

func handleJarhcSubmit(request events.APIGatewayProxyRequest) (events.APIGatewayProxyResponse, error) {

	// get message from request body
	message := request.Body
	log.Println("message: ", message)

	// parse JSON message
	var in JarhcCheckRequest
	reader := strings.NewReader(message)
	err := json.NewDecoder(reader).Decode(&in)
	if err != nil {
		log.Println("error parsing JSON message:", err)
		return sendError(http.StatusBadRequest, err)
	}

	for _, version := range in.Classpath {
		log.Println("classpath:", version)

		// check if version is valid
		if !isValidVersion(version) {
			log.Println("version is not valid:", version)
			return sendErrorMessage(http.StatusBadRequest, "Version is not valid: "+version)
		}

		// check if version exists in Maven Central
		artifact, _ := maven.NewArtifact(version)
		exists, _ := artifact.Exists()
		if !exists {
			log.Println("version not found in Maven Central:", version)
			return sendErrorMessage(http.StatusBadRequest, "Version not found in Maven Central: "+version)
		}
	}

	for _, version := range in.Provided {
		log.Println("provided:", version)

		// check if version is valid
		if !isValidVersion(version) {
			log.Println("version is not valid:", version)
			return sendErrorMessage(http.StatusBadRequest, "Version is not valid: "+version)
		}

		// check if version exists in Maven Central
		artifact, _ := maven.NewArtifact(version)
		exists, _ := artifact.Exists()
		if !exists {
			log.Println("version not found in Maven Central:", version)
			return sendErrorMessage(http.StatusBadRequest, "Version not found in Maven Central: "+version)
		}
	}

	// calculate hash for combination of versions
	config := strings.Join(in.Classpath, ";") + "/" + strings.Join(in.Provided, ";")
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

	payload := JarhcCheckMessage{
		Classpath:      in.Classpath,
		Provided:       in.Provided,
		ReportFileName: reportFileName,
	}

	err = lambdaClient.InvokeAsync("jarhc-check", payload)
	if err != nil {
		log.Println("error invoking Lambda function:", err)
		return sendError(http.StatusInternalServerError, err)
	}

	return sendReportFile(reportFileURL)
}
