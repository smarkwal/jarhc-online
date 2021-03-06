package main

import (
	"github.com/aws/aws-lambda-go/events"
)

func handleAuthValidate(request events.APIGatewayProxyRequest) (events.APIGatewayProxyResponse, error) {

	// prepare headers
	headers := make(map[string]string)
	headers["Content-Type"] = "text/plain"

	// return API response
	response := events.APIGatewayProxyResponse{
		StatusCode: 200,
		Headers:    headers,
		Body:       "OK",
	}
	return response, nil
}
