package main

import (
	"crypto/sha256"
	"encoding/json"
	"fmt"
	"github.com/aws/aws-lambda-go/events"
	"github.com/smarkwal/jarhc-online/sam-app/common/cloud"
	"github.com/smarkwal/jarhc-online/sam-app/common/maven"
	"log"
	"net/http"
	"os"
	"regexp"
	"strings"
)

type JapiccCheckRequest struct {
	OldVersion string `json:"oldVersion"`
	NewVersion string `json:"newVersion"`
}

type JapiccCheckResponse struct {
	ReportURL    string `json:"reportURL"`
	ErrorMessage string `json:"errorMessage"`
}

func handlerJapiccSubmit(request events.APIGatewayProxyRequest) (events.APIGatewayProxyResponse, error) {

	// get message from request body
	message := request.Body
	log.Println("message: ", message)

	// parse JSON message
	var in JapiccCheckRequest
	reader := strings.NewReader(message)
	err := json.NewDecoder(reader).Decode(&in)
	if err != nil {
		log.Println("error parsing JSON message:", err)
		return sendJapiccSubmitError(http.StatusBadRequest, err)
	}

	oldVersion := in.OldVersion
	log.Println("old version:", oldVersion)

	// check if old version is valid
	if !isValidVersion(oldVersion) {
		log.Println("version is not valid:", oldVersion)
		return sendJapiccSubmitErrorMessage(http.StatusBadRequest, "Version is not valid: "+oldVersion)
	}

	newVersion := in.NewVersion
	log.Println("new version:", newVersion)

	// check if new version is valid
	if !isValidVersion(newVersion) {
		log.Println("version is not valid:", newVersion)
		return sendJapiccSubmitErrorMessage(http.StatusBadRequest, "Version is not valid: "+newVersion)
	}

	if oldVersion == newVersion {
		log.Println("submitted same version twice:", oldVersion)
		return sendJapiccSubmitErrorMessage(http.StatusBadRequest, "Cannot compare version with itself: "+oldVersion)
	}

	// calculate hash for combination of versions
	hash := sha256hex(oldVersion + "/" + newVersion)

	// prepare report file
	reportFileName := "report-" + hash + ".html"
	log.Println("report file name:", reportFileName)

	// get S3 bucket
	region := "eu-central-1" // TODO: read from ENV
	var bucketName = os.Getenv("BUCKET_NAME")
	if len(bucketName) == 0 {
		bucketName = "localhost"
	}
	log.Println("bucket name:", bucketName)

	// check if report file already exists
	log.Println("connect to S3:", region, bucketName)
	s3 := cloud.NewS3Client(region, bucketName)
	exists, err := s3.Exists(reportFileName)
	if err != nil {
		log.Println("error testing if report file exists in S3 bucket:", err)
		return sendJapiccSubmitError(http.StatusInternalServerError, err)
	}

	reportFileURL := s3.GetURL(reportFileName)
	if exists {
		log.Println("report file found in S3:", reportFileURL)
		return sendReportFile(reportFileURL)
	}

	// check if old version exists in Maven Central
	oldArtifact, _ := maven.NewArtifact(oldVersion)
	exists, _ = oldArtifact.Exists()
	if !exists {
		log.Println("version not found in Maven Central:", oldVersion)
		return sendJapiccSubmitErrorMessage(http.StatusBadRequest, "Version not found in Maven Central: "+oldVersion)
	}

	// check if new version exists in Maven Central
	newArtifact, _ := maven.NewArtifact(newVersion)
	exists, _ = newArtifact.Exists()
	if !exists {
		log.Println("version not found in Maven Central:", newVersion)
		return sendJapiccSubmitErrorMessage(http.StatusBadRequest, "Version not found in Maven Central: "+newVersion)
	}

	// create SQS client
	log.Println("connect to SQS:", region)
	client, err := cloud.NewSQSClient(region)
	if err != nil {
		log.Println("error connecting to SQS:", err)
		return sendJapiccSubmitError(http.StatusInternalServerError, err)
	}

	// add message to SQS queue
	queueName := "japicc-job-queue" // TODO: get from env
	log.Println("send message to SQS:", queueName, message)
	messageId, err := client.SendMessage(queueName, message)
	if err != nil {
		log.Println("error sending message to SQS:", err)
		return sendJapiccSubmitError(http.StatusInternalServerError, err)
	}
	log.Println("message ID:", messageId)

	return sendReportFile(reportFileURL)
}

func isValidVersion(version string) bool {
	if len(version) < 5 {
		return false
	}
	matched, _ := regexp.MatchString(`^[^:]+:[^:]+:[^:]*[^.]$`, version)
	return matched
}

func sendReportFile(reportFileURL string) (events.APIGatewayProxyResponse, error) {

	// prepare headers
	headers := make(map[string]string)
	headers["Content-Type"] = "application/json"

	// prepare body
	var body = JapiccCheckResponse{
		ReportURL: reportFileURL,
	}

	// serialize body to JSON
	jsonBody, err := json.Marshal(body)

	// return API response
	response := events.APIGatewayProxyResponse{
		StatusCode: 200,
		Headers:    headers,
		Body:       string(jsonBody),
	}
	return response, err
}

func sendJapiccSubmitError(statusCode int, err error) (events.APIGatewayProxyResponse, error) {
	return sendJapiccSubmitErrorMessage(statusCode, err.Error())
}

func sendJapiccSubmitErrorMessage(statusCode int, errorMessage string) (events.APIGatewayProxyResponse, error) {

	// prepare headers
	headers := make(map[string]string)
	headers["Content-Type"] = "application/json"

	// prepare body
	var body = JapiccCheckResponse{
		ErrorMessage: errorMessage,
	}

	// serialize body to JSON
	jsonBody, err := json.Marshal(body)
	if err != nil {
		log.Println("error serializing response to JSON:", err)
	}

	// return API response
	response := events.APIGatewayProxyResponse{
		StatusCode: statusCode,
		Headers:    headers,
		Body:       string(jsonBody),
	}
	return response, nil
}

func sha256hex(text string) string {
	data := []byte(text)
	hash := sha256.Sum256(data)
	return fmt.Sprintf("%x", hash[:])
}
