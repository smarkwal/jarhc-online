package main

import (
	"context"
	"github.com/aws/aws-lambda-go/lambda"
	"github.com/smarkwal/jarhc-online/sam-app/common/cloud"
	"github.com/smarkwal/jarhc-online/sam-app/jarhc-check/jarhc"
	"log"
	"os"
	"path"
)

type JarhcCheckRequest struct {
	Classpath      []string `json:"classpath"`
	Provided       []string `json:"provided"`
	ReportFileName string   `json:"reportFileName"`
}

var (
	region     = "eu-central-1" // TODO: read from ENV
	bucketName = os.Getenv("BUCKET_NAME")
	s3Client   cloud.S3Client
)

func init() {
	if len(bucketName) == 0 {
		bucketName = "localhost"
	}
	s3Client = cloud.NewS3Client(region, bucketName)
}

func main() {
	lambda.Start(handler)
}

func handler(ctx context.Context, request JarhcCheckRequest) error {

	// prepare report file
	reportFileName := request.ReportFileName

	// check if report file already exists
	exists, err := s3Client.Exists(reportFileName)
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
	_, err = s3Client.Upload(reportFileName, file)
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
