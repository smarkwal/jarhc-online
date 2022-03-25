package main

import (
	"context"
	"crypto/sha256"
	"fmt"
	"github.com/aws/aws-lambda-go/lambda"
	"github.com/smarkwal/jarhc-online/sam-app/common/cloud"
	"github.com/smarkwal/jarhc-online/sam-app/jarhc-check/jarhc"
	"log"
	"os"
	"path"
	"strings"
)

var region = "eu-central-1" // TODO: read from ENV
var bucketName = os.Getenv("BUCKET_NAME")

type JarhcCheckRequest struct {
	Classpath []string `json:"classpath"`
	Provided  []string `json:"provided"`
}

func init() {
	// set up global state
	if len(bucketName) == 0 {
		bucketName = "localhost"
	}
	// TODO: create and reuse S3 client?
}

func main() {
	lambda.Start(handler)
}

func handler(ctx context.Context, request JarhcCheckRequest) error {

	config := strings.Join(request.Classpath, ";") + "/" + strings.Join(request.Provided, ";")

	// calculate hash for combination of versions
	hash := sha256hex(config)

	// prepare report file
	reportFileName := "report-" + hash + ".html"

	// check if report file already exists
	s3 := cloud.NewS3Client(region, bucketName)
	exists, err := s3.Exists(reportFileName)
	if err != nil {
		return err
	}
	if exists {
		log.Println("report file size: [S3]")
		return nil
	}

	// prepare temp dir for report file
	reportFilePath := path.Join(os.TempDir(), "reports", reportFileName)
	log.Println("report file path:", reportFilePath)

	err = os.MkdirAll(path.Dir(reportFilePath), os.ModePerm)
	if err != nil {
		return err
	}

	// TODO: check all artifacts

	options := jarhc.Options{
		Classpath: request.Classpath,
		Provided:  request.Provided,
	}

	err = jarhc.Check(options, reportFilePath)
	if err != nil {
		return err
	}
	log.Println("report file size: ", getFileSize(reportFilePath))

	// read report file
	file, err := os.Open(reportFilePath)
	if err != nil {
		return err
	}
	//goland:noinspection GoUnhandledErrorResult
	defer file.Close()

	// upload report file to S3
	_, err = s3.Upload(reportFileName, file)
	if err != nil {
		return err
	}

	return nil
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
