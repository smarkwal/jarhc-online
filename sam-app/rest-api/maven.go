package main

import (
	"encoding/json"
	"github.com/aws/aws-lambda-go/events"
	"github.com/smarkwal/jarhc-online/sam-app/common/maven"
	"log"
	"net/http"
	"strings"
)

type MavenSearchRequest struct {
	Coordinates string `json:"coordinates"`
}

type MavenSearchResponse struct {
	Coordinates string           `json:"coordinates"`
	Artifacts   []maven.Artifact `json:"artifacts"`
}

type MavenSearchError struct {
	ErrorMessage string `json:"errorMessage"`
}

func handleMavenSearch(request events.APIGatewayProxyRequest) (events.APIGatewayProxyResponse, error) {

	// get message from request body
	message := request.Body
	log.Println("message: ", message)

	// parse JSON message
	var in MavenSearchRequest
	reader := strings.NewReader(message)
	err := json.NewDecoder(reader).Decode(&in)
	if err != nil {
		log.Println("error parsing JSON message: ", err)
		return sendMavenSearchError(http.StatusBadRequest, err)
	}

	// get coordinates from query string
	coordinates := in.Coordinates
	if len(coordinates) == 0 {
		return sendMavenSearchErrorMessage(http.StatusBadRequest, "missing parameter: 'coordinates'")
	}

	// parse coordinates
	artifact, err := maven.NewArtifact(coordinates)
	if err != nil {
		return sendMavenSearchError(http.StatusBadRequest, err)
	}

	// check if artifact exists
	exists, err := artifact.Exists()
	if err != nil {
		return sendMavenSearchError(http.StatusInternalServerError, err)
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

	// prepare response body
	out := MavenSearchResponse{
		Coordinates: coordinates,
		Artifacts:   artifacts,
	}

	// serialize body to JSON
	jsonBody, err := json.Marshal(out)

	// return API response
	response := events.APIGatewayProxyResponse{
		StatusCode: 200,
		Headers:    headers,
		Body:       string(jsonBody),
	}
	return response, err
}

func sendMavenSearchError(statusCode int, err error) (events.APIGatewayProxyResponse, error) {
	return sendMavenSearchErrorMessage(statusCode, err.Error())
}

func sendMavenSearchErrorMessage(statusCode int, errorMessage string) (events.APIGatewayProxyResponse, error) {

	log.Println("Error:", errorMessage)

	// prepare headers
	headers := make(map[string]string)
	headers["Content-Type"] = "application/json"

	// prepare response body
	out := MavenSearchError{
		ErrorMessage: errorMessage,
	}

	// serialize body to JSON
	jsonBody, err := json.Marshal(out)

	// return API response
	response := events.APIGatewayProxyResponse{
		StatusCode: statusCode,
		Headers:    headers,
		Body:       string(jsonBody),
	}
	return response, err
}
