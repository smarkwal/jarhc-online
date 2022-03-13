package main

import (
	"github.com/aws/aws-lambda-go/events"
	"github.com/aws/aws-lambda-go/lambda"
	"log"
)

func main() {
	lambda.Start(handler)
}

func handler(request events.APIGatewayProxyRequest) (events.APIGatewayProxyResponse, error) {

	// log.Println("-----------------------------------------------------------")
	// log.Println("Headers:")
	// for name, value := range request.Headers {
	// 	log.Printf("\t%s: %s\n", name, value)
	// }
	// log.Println("-----------------------------------------------------------")

	response, err := dispatch(request)

	// add CORS headers to every response
	// (reflect origin header)
	origin := getOrigin(request)
	addCorsHeaders(response.Headers, origin)

	return response, err
}

func dispatch(request events.APIGatewayProxyRequest) (events.APIGatewayProxyResponse, error) {

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

func getOrigin(request events.APIGatewayProxyRequest) string {

	// live behind AWS API Gateway
	origin := request.Headers["origin"]
	if len(origin) > 0 {
		return origin
	}

	// test on localhost with `sam local start-api`
	origin = request.Headers["Origin"]
	if len(origin) > 0 {
		return origin
	}

	// fallback
	return "*"
}

func addCorsHeaders(headers map[string]string, origin string) {
	log.Println("CORS Origin:", origin)
	headers["Access-Control-Allow-Origin"] = origin
	headers["Access-Control-Allow-Credentials"] = "true"
}
