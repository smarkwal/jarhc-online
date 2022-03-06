package main

import (
	"github.com/aws/aws-lambda-go/events"
	"github.com/aws/aws-lambda-go/lambda"
	"os"
)

func main() {
	lambda.Start(handler)
}

func handler(request events.APIGatewayProxyRequest) (events.APIGatewayProxyResponse, error) {

	// prepare headers
	headers := make(map[string]string)
	headers["Content-Type"] = "text/plain"
	addCorsHeaders(headers)

	// return API response
	response := events.APIGatewayProxyResponse{
		StatusCode: 200,
		Headers:    headers,
		Body:       "OK",
	}
	return response, nil
}

func addCorsHeaders(headers map[string]string) {
	var websiteUrl = os.Getenv("WEBSITE_URL")
	if len(websiteUrl) == 0 {
		websiteUrl = "http://localhost:3000"
	}
	headers["Access-Control-Allow-Origin"] = websiteUrl
	headers["Access-Control-Allow-Credentials"] = "true"
}
