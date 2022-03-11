package main

import (
	"github.com/aws/aws-lambda-go/events"
	"github.com/aws/aws-lambda-go/lambda"
)

func main() {
	lambda.Start(handler)
}

func handler(request events.APIGatewayProxyRequest) (events.APIGatewayProxyResponse, error) {

	// dispatch request to correct handler
	if request.Path == "/maven/search" {
		return handlerMavenSearch(request)
	} else if request.Path == "/japicc/submit" {
		return handlerJapiccSubmit(request)
	} else if request.Path == "/auth/validate" {
		return handlerAuthValidate(request)
	} else {
		// return "404 - not found" error response
		response := events.APIGatewayProxyResponse{
			StatusCode: 404,
		}
		return response, nil
	}

}
