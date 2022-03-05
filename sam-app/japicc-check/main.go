package main

import (
	"context"
	"crypto/sha256"
	"encoding/json"
	"fmt"
	"github.com/aws/aws-lambda-go/events"
	"github.com/aws/aws-lambda-go/lambda"
	"github.com/smarkwal/jarhc-online/sam-app/common/cloud"
	"github.com/smarkwal/jarhc-online/sam-app/common/maven"
	"github.com/smarkwal/jarhc-online/sam-app/japicc-check/japicc"
	"log"
	"os"
	"path"
	"strings"
)

var tempDirPath = path.Join(os.TempDir(), "jarhc-online")

type JapiccCheckRequest struct {
	OldVersion string `json:"oldVersion"`
	NewVersion string `json:"newVersion"`
}

func main() {
	lambda.Start(handler)
}

func handler(ctx context.Context, sqsEvent events.SQSEvent) error {
	for _, message := range sqsEvent.Records {
		fmt.Printf("The message %s for event source %s = %s \n", message.MessageId, message.EventSource, message.Body)

		// TODO: delete message?

		// handle message
		err := handle(message)
		if err != nil {
			// TODO: exception handling
			log.Println("Error:", err)
		}
	}

	return nil
}

func handle(message events.SQSMessage) error {

	// parse JSON request body
	var in JapiccCheckRequest
	reader := strings.NewReader(message.Body)
	err := json.NewDecoder(reader).Decode(&in)
	if err != nil {
		return err
	}

	oldVersion := in.OldVersion
	log.Println("old version:", oldVersion)

	newVersion := in.NewVersion
	log.Println("new version:", newVersion)

	// calculate hash for combination of versions
	hash := sha256hex(oldVersion + "/" + newVersion)

	// prepare report file
	reportFileName := "report-" + hash + ".html"

	// get S3 bucket
	region := "eu-central-1" // TODO: read from ENV
	var bucketName = os.Getenv("BUCKET_NAME")
	if len(bucketName) == 0 {
		bucketName = "localhost"
	}

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
	reportFilePath := path.Join(tempDirPath, "reports", reportFileName)
	log.Println("report file path:", reportFilePath)

	err = os.MkdirAll(path.Dir(reportFilePath), os.ModePerm)
	if err != nil {
		return err
	}

	oldArtifact, err := maven.NewArtifact(oldVersion)
	if err != nil {
		return err
	}
	log.Println("old artifact:", oldArtifact.ToCoordinates())

	newArtifact, err := maven.NewArtifact(newVersion)
	if err != nil {
		return err
	}
	log.Println("new artifact:", newArtifact.ToCoordinates())

	oldFilePath := path.Join(tempDirPath, oldArtifact.ToPath())
	log.Println("old file path:", oldFilePath)
	err = os.MkdirAll(path.Dir(oldFilePath), os.ModePerm)
	if err != nil {
		return err
	}
	err = oldArtifact.Download(oldFilePath)
	if err != nil {
		return err
	}
	log.Println("old file size: ", getFileSize(oldFilePath))

	newFilePath := path.Join(tempDirPath, newArtifact.ToPath())
	log.Println("new file path:", newFilePath)
	err = os.MkdirAll(path.Dir(newFilePath), os.ModePerm)
	if err != nil {
		return err
	}
	err = newArtifact.Download(newFilePath)
	if err != nil {
		return err
	}
	log.Println("new file size: ", getFileSize(newFilePath))

	err = japicc.Check(oldFilePath, newFilePath, reportFilePath)
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
