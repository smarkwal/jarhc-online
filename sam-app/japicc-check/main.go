package main

import (
	"crypto/sha256"
	"encoding/json"
	"fmt"
	"github.com/aws/aws-lambda-go/events"
	"github.com/aws/aws-lambda-go/lambda"
	"github.com/smarkwal/jarhc-online/sam-app/japicc-check/japicc"
	"github.com/smarkwal/jarhc-online/sam-app/japicc-check/maven"
	"github.com/smarkwal/jarhc-online/sam-app/japicc-check/reports"
	"log"
	"net/http"
	"os"
	"path"
	"strings"
)

var tempDirPath = path.Join(os.TempDir(), "jarhc-online")

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

	// parse JSON request body
	var in JapiccCheckRequest
	reader := strings.NewReader(request.Body)
	err := json.NewDecoder(reader).Decode(&in)
	if err != nil {
		return sendError(http.StatusBadRequest, err)
	}

	oldVersion := in.OldVersion
	log.Println("old version:", oldVersion)

	newVersion := in.NewVersion
	log.Println("new version:", newVersion)

	// calculate hash for combination of versions
	hash := sha256hex(oldVersion + "/" + newVersion)

	// prepare report file
	reportFileName := "report-" + hash + ".html"

	// get S3 bucket
	region := "eu-central-1" // TODO: read from ENV
	var bucketName = os.Getenv("BUCKET_NAME")
	if len(bucketName) == 0 {
		bucketName = "localhost"
	}

	// check if report file already exists
	s3 := reports.NewS3Client(region, bucketName)
	exists, err := s3.Exists(reportFileName)
	if err != nil {
		return sendError(http.StatusInternalServerError, err)
	}

	reportFileURL := s3.GetURL(reportFileName)
	if exists {
		log.Println("report file size: [S3]")
		return sendReportFile(reportFileURL)
	}

	// prepare temp dir for report file
	reportFilePath := path.Join(tempDirPath, "reports", reportFileName)
	log.Println("report file path:", reportFilePath)

	err = os.MkdirAll(path.Dir(reportFilePath), os.ModePerm)
	if err != nil {
		return sendError(http.StatusInternalServerError, err)
	}

	oldArtifact, err := maven.NewArtifact(oldVersion)
	if err != nil {
		return sendError(http.StatusBadRequest, err)
	}
	log.Println("old artifact:", oldArtifact.ToCoordinates())

	newArtifact, err := maven.NewArtifact(newVersion)
	if err != nil {
		return sendError(http.StatusBadRequest, err)
	}
	log.Println("new artifact:", newArtifact.ToCoordinates())

	oldFilePath := path.Join(tempDirPath, oldArtifact.ToPath())
	log.Println("old file path:", oldFilePath)
	err = os.MkdirAll(path.Dir(oldFilePath), os.ModePerm)
	if err != nil {
		return sendError(http.StatusInternalServerError, err)
	}
	err = oldArtifact.Download(oldFilePath)
	if err != nil {
		return sendError(http.StatusInternalServerError, err)
	}
	log.Println("old file size: ", getFileSize(oldFilePath))

	newFilePath := path.Join(tempDirPath, newArtifact.ToPath())
	log.Println("new file path:", newFilePath)
	err = os.MkdirAll(path.Dir(newFilePath), os.ModePerm)
	if err != nil {
		return sendError(http.StatusInternalServerError, err)
	}
	err = newArtifact.Download(newFilePath)
	if err != nil {
		return sendError(http.StatusInternalServerError, err)
	}
	log.Println("new file size: ", getFileSize(newFilePath))

	err = japicc.Check(oldFilePath, newFilePath, reportFilePath)
	if err != nil {
		return sendError(http.StatusInternalServerError, err)
	}
	log.Println("report file size: ", getFileSize(reportFilePath))

	// read report file
	file, err := os.Open(reportFilePath)
	if err != nil {
		return sendError(http.StatusInternalServerError, err)
	}
	//goland:noinspection GoUnhandledErrorResult
	defer file.Close()

	// upload report file to S3
	_, err = s3.Upload(reportFileName, file)
	if err != nil {
		return sendError(http.StatusInternalServerError, err)
	}

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
		log.Println("JSON error: ", err)
	}

	// return API response
	response := events.APIGatewayProxyResponse{
		StatusCode: statusCode,
		Headers:    headers,
		Body:       string(jsonBody),
	}
	return response, nil
}

func getFileSize(path string) int64 {
	info, err := os.Stat(path)
	if err != nil {
		return -1
	}
	return info.Size()
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
