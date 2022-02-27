package main

import (
	"crypto/sha256"
	"encoding/json"
	"fmt"
	"github.com/aws/aws-lambda-go/events"
	"github.com/aws/aws-lambda-go/lambda"
	"github.com/smarkwal/jarhc-online/sam-app/japicc-check/japicc"
	"github.com/smarkwal/jarhc-online/sam-app/japicc-check/maven"
	"log"
	"net/url"
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
		// TODO: return bad request
		return events.APIGatewayProxyResponse{}, err
	}

	oldVersion := in.OldVersion
	log.Println("old version:", oldVersion)

	newVersion := in.NewVersion
	log.Println("old version:", newVersion)

	// calculate hash for combination of versions
	hash := sha256hex(oldVersion + "/" + newVersion)

	// prepare report file
	reportFileName := "report-" + hash + ".html"
	reportFilePath := path.Join(tempDirPath, "reports", reportFileName)
	log.Println("report file path:", reportFilePath)

	// check if report file already exists
	info, err := os.Stat(reportFilePath)
	if err == nil && info != nil && !info.IsDir() {
		log.Println("report file size: ", getFileSize(reportFilePath))

		return returnReportFile(reportFileName, err)
	}

	err = os.MkdirAll(path.Dir(reportFilePath), os.ModePerm)
	if err != nil {
		// TODO: return internal server error
		return events.APIGatewayProxyResponse{}, err
	}

	oldArtifact, err := maven.NewArtifact(oldVersion)
	if err != nil {
		// TODO: return bad request
		return events.APIGatewayProxyResponse{}, err
	}
	log.Println("old artifact:", oldArtifact.ToCoordinates())

	newArtifact, err := maven.NewArtifact(newVersion)
	if err != nil {
		// TODO: return bad request
		return events.APIGatewayProxyResponse{}, err
	}
	log.Println("new artifact:", newArtifact.ToCoordinates())

	oldFilePath := path.Join(tempDirPath, oldArtifact.ToPath())
	log.Println("old file path:", oldFilePath)
	err = os.MkdirAll(path.Dir(oldFilePath), os.ModePerm)
	if err != nil {
		// TODO: return internal server error
		return events.APIGatewayProxyResponse{}, err
	}
	err = oldArtifact.Download(oldFilePath)
	if err != nil {
		// TODO: return internal server error
		return events.APIGatewayProxyResponse{}, err
	}
	log.Println("old file size: ", getFileSize(oldFilePath))

	newFilePath := path.Join(tempDirPath, newArtifact.ToPath())
	log.Println("new file path:", newFilePath)
	err = os.MkdirAll(path.Dir(newFilePath), os.ModePerm)
	if err != nil {
		// TODO: return internal server error
		return events.APIGatewayProxyResponse{}, err
	}
	err = newArtifact.Download(newFilePath)
	if err != nil {
		// TODO: return internal server error
		return events.APIGatewayProxyResponse{}, err
	}
	log.Println("new file size: ", getFileSize(newFilePath))

	err = japicc.Check(oldFilePath, newFilePath, reportFilePath)
	if err != nil {
		// TODO: return internal server error
		return events.APIGatewayProxyResponse{}, err
	}
	log.Println("report file size: ", getFileSize(reportFilePath))

	return returnReportFile(reportFileName, err)
}

func returnReportFile(reportFileName string, err error) (events.APIGatewayProxyResponse, error) {

	// prepare JSON response
	var r JapiccCheckResponse
	if err == nil {
		r.ReportURL = "http://localhost:8080/japicc/report/" + url.PathEscape(reportFileName)
	} else {
		r.ErrorMessage = err.Error()
	}

	// serialize to JSON
	body, err := json.Marshal(r)

	// wrap response for API gateway
	response := events.APIGatewayProxyResponse{
		StatusCode: 200,
		Body:       string(body),
	}

	return response, err
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
