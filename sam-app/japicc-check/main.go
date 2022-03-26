package main

import (
	"context"
	"fmt"
	"github.com/aws/aws-lambda-go/lambda"
	"github.com/smarkwal/jarhc-online/sam-app/common/cloud"
	"github.com/smarkwal/jarhc-online/sam-app/common/maven"
	"github.com/smarkwal/jarhc-online/sam-app/japicc-check/japicc"
	"log"
	"os"
	"path"
	"regexp"
	"strings"
)

const reportFileNamePattern = "^[a-z][A-Za-z0-9-.]*\\.html$"

type JapiccCheckRequest struct {
	OldVersion     string `json:"oldVersion"`
	NewVersion     string `json:"newVersion"`
	ReportFileName string `json:"reportFileName"`
}

var (
	region      = os.Getenv("AWS_REGION")
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

	// TODO: log username

	// log request -------------------------------------------------------------

	oldVersion := request.OldVersion
	newVersion := request.NewVersion
	reportFileName := request.ReportFileName
	log.Println("oldVersion:", oldVersion)
	log.Println("newVersion:", newVersion)
	log.Println("reportFileName:", reportFileName)

	// validate request --------------------------------------------------------

	err := validateRequest(request)
	if err != nil {
		return fmt.Errorf("request validation failed: %w", err)
	}

	// check if report file already exists in S3 -------------------------------

	exists, err := s3Client.Exists(reportFileName)
	if err != nil {
		return fmt.Errorf("error checking if report file exists in S3: %w", err)
	}
	if exists {
		log.Println("report file already exists in S3:", reportFileName)
		return nil
	}

	// download libraries from Maven Central -----------------------------------

	oldFilePath, err := downloadLibrary(oldVersion)
	if err != nil {
		return fmt.Errorf("error downloading old library from Maven Central: %w", err)
	}

	newFilePath, err := downloadLibrary(newVersion)
	if err != nil {
		return fmt.Errorf("error downloading new library from Maven Central: %w", err)
	}

	// run JAPICC --------------------------------------------------------------

	// prepare temp dir for report file
	reportFilePath := path.Join(tempDirPath, "reports", reportFileName)
	err = os.MkdirAll(path.Dir(reportFilePath), os.ModePerm)
	if err != nil {
		return fmt.Errorf("error creating temp directory for report file: %w", err)
	}

	// execute check
	err = japicc.Check(oldFilePath, newFilePath, reportFilePath)
	if err != nil {
		return fmt.Errorf("error running JAPICC: %w", err)
	}

	// upload report to S3 -----------------------------------------------------

	// read report file
	file, err := os.Open(reportFilePath)
	if err != nil {
		return fmt.Errorf("error opening report file: %w", err)
	}
	//goland:noinspection GoUnhandledErrorResult
	defer file.Close()

	// upload report file to S3
	_, err = s3Client.Upload(reportFileName, file)
	if err != nil {
		return fmt.Errorf("error uploading report file to S3: %w", err)
	}

	return nil
}

func validateRequest(request JapiccCheckRequest) error {

	// validate old library
	oldVersion := request.OldVersion
	if len(oldVersion) == 0 {
		return fmt.Errorf("parameter 'oldVersion' must not be empty")
	}
	err := validateLibrary(oldVersion)
	if err != nil {
		return err
	}

	// validate new library
	newVersion := request.NewVersion
	if len(newVersion) == 0 {
		return fmt.Errorf("parameter 'newVersion' must not be empty")
	}
	err = validateLibrary(newVersion)
	if err != nil {
		return err
	}

	// validate report file name
	reportFileName := request.ReportFileName
	err = validateReportFileName(reportFileName)
	if err != nil {
		return fmt.Errorf("error validating report file name: %w", err)
	}

	return nil
}

func validateLibrary(library string) error {
	artifact, err := maven.NewArtifact(library)
	if err != nil {
		return fmt.Errorf("error parsing library coordinates: %w", err)
	}
	exists, err := artifact.Exists()
	if err != nil {
		return fmt.Errorf("error checking if library exists in Maven Central: %w", err)
	}
	if !exists {
		return fmt.Errorf("library not found in Maven Central: %s", library)
	}
	return nil
}

func validateReportFileName(reportFileName string) error {
	if len(reportFileName) == 0 {
		return fmt.Errorf("parameter 'reportFileName' not found")
	} else if !strings.HasSuffix(reportFileName, ".html") {
		return fmt.Errorf("report file format not supported: %s", reportFileName)
	} else if match, _ := regexp.MatchString(reportFileNamePattern, reportFileName); !match {
		return fmt.Errorf("invalid report file name: %s", reportFileName)
	}
	return nil
}

func downloadLibrary(library string) (string, error) {

	artifact, _ := maven.NewArtifact(library)
	filePath := path.Join(tempDirPath, artifact.ToPath())
	err := os.MkdirAll(path.Dir(filePath), os.ModePerm)
	if err != nil {
		return "", fmt.Errorf("error creating temp directory for library download: %w", err)
	}

	err = artifact.Download(filePath)
	if err != nil {
		return "", fmt.Errorf("error downloading library: %w", err)
	}

	return filePath, nil
}
