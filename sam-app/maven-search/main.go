package main

import (
	"encoding/json"
	"github.com/aws/aws-lambda-go/events"
	"github.com/aws/aws-lambda-go/lambda"
	"github.com/smarkwal/jarhc-online/sam-app/common/maven"
	"log"
	"net/http"
	"os"
)

func main() {
	lambda.Start(handler)
}

func handler(request events.APIGatewayProxyRequest) (events.APIGatewayProxyResponse, error) {

	// get coordinates from query string
	query := request.QueryStringParameters
	coordinates := query["coordinates"]
	if len(coordinates) == 0 {
		return sendErrorMessage(http.StatusBadRequest, "missing parameter: 'coordinates'")
	}

	// parse coordinates
	artifact, err := maven.NewArtifact(coordinates)
	if err != nil {
		return sendError(http.StatusBadRequest, err)
	}

	// check if artifact exists
	exists, err := artifact.Exists()
	if err != nil {
		return sendError(http.StatusInternalServerError, err)
	}

	// prepare list of artifacts
	//goland:noinspection ALL
	artifacts := []maven.Artifact{}
	if exists {
		artifacts = append(artifacts, *artifact)
	}

	// prepare headers
	headers := make(map[string]string)
	headers["Content-Type"] = "application/json"
	addCorsHeaders(headers)

	// serialize body to JSON
	jsonBody, err := json.Marshal(artifacts)

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

	log.Println("Error:", errorMessage)

	// prepare headers
	headers := make(map[string]string)
	headers["Content-Type"] = "application/json"
	addCorsHeaders(headers)

	// return API response
	response := events.APIGatewayProxyResponse{
		StatusCode: statusCode,
		Headers:    headers,
		Body:       "[]",
	}
	return response, nil
}

func addCorsHeaders(headers map[string]string) {
	var websiteUrl = os.Getenv("WEBSITE_URL")
	if len(websiteUrl) == 0 {
		websiteUrl = "http://localhost:3000"
	}
	headers["Access-Control-Allow-Origin"] = websiteUrl
}
