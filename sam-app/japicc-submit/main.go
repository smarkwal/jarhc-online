package main

import (
	"crypto/sha256"
	"encoding/json"
	"fmt"
	"github.com/aws/aws-lambda-go/events"
	"github.com/aws/aws-lambda-go/lambda"
	"github.com/smarkwal/jarhc-online/sam-app/common/cloud"
	"log"
	"net/http"
	"os"
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

func main() {
	lambda.Start(handler)
}

func handler(request events.APIGatewayProxyRequest) (events.APIGatewayProxyResponse, error) {

	// get message from request body
	message := request.Body
	log.Println("message: ", message)

	// parse JSON message
	var in JapiccCheckRequest
	reader := strings.NewReader(message)
	err := json.NewDecoder(reader).Decode(&in)
	if err != nil {
		log.Println("error parsing JSON message: ", err)
		return sendError(http.StatusBadRequest, err)
	}

	// TODO: validate old and new version (syntax)

	oldVersion := in.OldVersion
	log.Println("old version:", oldVersion)

	newVersion := in.NewVersion
	log.Println("new version:", newVersion)

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
		return sendError(http.StatusInternalServerError, err)
	}

	reportFileURL := s3.GetURL(reportFileName)
	if exists {
		log.Println("report file found in S3:", reportFileURL)
		return sendReportFile(reportFileURL)
	}

	// TODO: check if library versions exist in Maven Central

	// create SQS client
	log.Println("connect to SQS:", region)
	client, err := cloud.NewSQSClient(region)
	if err != nil {
		log.Println("error connecting to SQS:", err)
		return sendError(http.StatusInternalServerError, err)
	}

	// add message to SQS queue
	queueName := "japicc-job-queue" // TODO: get from env
	log.Println("send message to SQS:", queueName, message)
	messageId, err := client.SendMessage(queueName, message)
	if err != nil {
		log.Println("error sending message to SQS:", err)
		return sendError(http.StatusInternalServerError, err)
	}
	log.Println("message ID:", messageId)

	return sendReportFile(reportFileURL)
}

func sendReportFile(reportFileURL string) (events.APIGatewayProxyResponse, error) {

	// prepare headers
	headers := make(map[string]string)
	headers["Content-Type"] = "application/json"
	addCorsHeaders(headers)

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

func sendError(statusCode int, err error) (events.APIGatewayProxyResponse, error) {
	return sendErrorMessage(statusCode, err.Error())
}

func sendErrorMessage(statusCode int, errorMessage string) (events.APIGatewayProxyResponse, error) {

	// prepare headers
	headers := make(map[string]string)
	headers["Content-Type"] = "application/json"
	addCorsHeaders(headers)

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

func addCorsHeaders(headers map[string]string) {
	var websiteUrl = os.Getenv("WEBSITE_URL")
	if len(websiteUrl) == 0 {
		websiteUrl = "http://localhost:3000"
	}
	headers["Access-Control-Allow-Origin"] = websiteUrl
}
