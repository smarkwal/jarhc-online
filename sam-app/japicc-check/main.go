package main

import (
	"context"
	"github.com/aws/aws-lambda-go/lambda"
	"github.com/smarkwal/jarhc-online/sam-app/common/cloud"
	"github.com/smarkwal/jarhc-online/sam-app/common/maven"
	"github.com/smarkwal/jarhc-online/sam-app/japicc-check/japicc"
	"log"
	"os"
	"path"
)

type JapiccCheckRequest struct {
	OldVersion     string `json:"oldVersion"`
	NewVersion     string `json:"newVersion"`
	ReportFileName string `json:"reportFileName"`
}

var (
	region      = "eu-central-1" // TODO: read from ENV
	bucketName  = os.Getenv("BUCKET_NAME")
	tempDirPath = path.Join(os.TempDir(), "jarhc-online")
	s3Client    cloud.S3Client
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

func handler(ctx context.Context, request JapiccCheckRequest) error {

	oldVersion := request.OldVersion
	log.Println("old version:", oldVersion)

	newVersion := request.NewVersion
	log.Println("new version:", newVersion)

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
