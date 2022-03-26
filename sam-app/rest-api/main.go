package main

import (
	"crypto/sha256"
	"encoding/json"
	"fmt"
	"github.com/aws/aws-lambda-go/events"
	"github.com/aws/aws-lambda-go/lambda"
	"github.com/smarkwal/jarhc-online/sam-app/common/cloud"
	"log"
	"os"
	"regexp"
)

var (
	region       = os.Getenv("AWS_REGION")
	bucketName   = os.Getenv("BUCKET_NAME")
	s3Client     cloud.S3Client
	lambdaClient *cloud.LambdaClient
)

func init() {

	if len(bucketName) == 0 {
		bucketName = "localhost"
	}

	s3Client = cloud.NewS3Client(region, bucketName)

	var err error
	lambdaClient, err = cloud.NewLambdaClient(region)
	if err != nil {
		panic(err)
	}

}

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

	// get current user
	user := getUser(request.RequestContext)
	log.Println(user)

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
		return handleMavenSearch(request)
	} else if request.Path == "/japicc/submit" {
		return handleJapiccSubmit(request)
	} else if request.Path == "/jarhc/submit" {
		return handleJarhcSubmit(request)
	} else if request.Path == "/auth/validate" {
		return handleAuthValidate(request)
	} else {
		return handleNotFound()
	}
}

func handleNotFound() (events.APIGatewayProxyResponse, error) {

	// return "404 - not found" error response
	response := events.APIGatewayProxyResponse{
		StatusCode: 404,
	}

	return response, nil
}

type User struct {
	Subject   string `json:"subject"`
	Email     string `json:"email"`
	SourceIP  string `json:"sourceIP"`
	UserAgent string `json:"userAgent"`
}

func (user User) String() string {
	return fmt.Sprintf("User { Subject: \"%s\", Email: \"%s\", SourceIP: \"%s\", UserAgent: \"%s\" }", user.Subject, user.Email, user.SourceIP, user.UserAgent)
}

func getUser(context events.APIGatewayProxyRequestContext) User {
	authorizer := context.Authorizer
	identity := context.Identity

	subject := ""
	email := ""
	claims, found := authorizer["claims"]
	if found {
		values := claims.(map[string]interface{})
		subject = values["sub"].(string)
		email = values["email"].(string)
	}
	sourceIP := identity.SourceIP
	userAgent := identity.UserAgent

	return User{Subject: subject, Email: email, SourceIP: sourceIP, UserAgent: userAgent}
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

func isValidVersion(version string) bool {
	if len(version) < 5 {
		return false
	}
	matched, _ := regexp.MatchString(`^[^:]+:[^:]+:[^:]*[^.]$`, version)
	return matched
}

func sha256hex(text string) string {
	data := []byte(text)
	hash := sha256.Sum256(data)
	return fmt.Sprintf("%x", hash[:])
}

type CheckResponse struct {
	ReportURL    string `json:"reportURL"`
	ErrorMessage string `json:"errorMessage"`
}

func sendReportFile(reportFileURL string) (events.APIGatewayProxyResponse, error) {

	// prepare headers
	headers := make(map[string]string)
	headers["Content-Type"] = "application/json"

	// prepare body
	var body = CheckResponse{
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

	// prepare body
	var body = CheckResponse{
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
