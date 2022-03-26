package main

import (
	"context"
	"fmt"
	"github.com/aws/aws-lambda-go/lambda"
	"github.com/smarkwal/jarhc-online/sam-app/common/cloud"
	"github.com/smarkwal/jarhc-online/sam-app/common/maven"
	"github.com/smarkwal/jarhc-online/sam-app/jarhc-check/jarhc"
	"log"
	"os"
	"path"
	"regexp"
	"strings"
)

const reportFileNamePattern = "^[a-z][A-Za-z0-9-.]*(\\.html|\\.txt)$"

type JarhcCheckRequest struct {
	Classpath      []string `json:"classpath"`
	Provided       []string `json:"provided"`
	ReportFileName string   `json:"reportFileName"`
}

var (
	region     = os.Getenv("AWS_REGION")
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

	// TODO: log username

	// log request -------------------------------------------------------------

	classpath := request.Classpath
	provided := request.Provided
	reportFileName := request.ReportFileName
	log.Println("classpath:", classpath)
	log.Println("provided:", provided)
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

	// run JarHC check ---------------------------------------------------------

	// prepare temp dir for report file
	reportFilePath := path.Join(os.TempDir(), "reports", reportFileName)
	err = os.MkdirAll(path.Dir(reportFilePath), os.ModePerm)
	if err != nil {
		return fmt.Errorf("error creating temp directory for report file: %w", err)
	}

	// prepare command options
	options := jarhc.Options{
		Classpath: classpath,
		Provided:  provided,
	}

	// execute check
	err = jarhc.Check(options, reportFilePath)
	if err != nil {
		return fmt.Errorf("error running JarHC: %w", err)
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

func validateRequest(request JarhcCheckRequest) error {

	// validate classpath libraries
	classpath := request.Classpath
	if len(classpath) == 0 {
		return fmt.Errorf("parameter 'classpath' must not be empty")
	} else if len(classpath) > 10 {
		return fmt.Errorf("parameter 'classpath' must not contain more than 10 values")
	}
	err := validateLibraries(classpath)
	if err != nil {
		return err
	}

	// validate provided libraries
	provided := request.Provided
	if len(provided) > 10 {
		return fmt.Errorf("parameter 'provided' must not contain more than 10 values")
	}
	err = validateLibraries(provided)
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

func validateLibraries(libraries []string) error {
	for _, library := range libraries {
		err := validateLibrary(library)
		if err != nil {
			return err
		}
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
	} else if !strings.HasSuffix(reportFileName, ".html") && !strings.HasSuffix(reportFileName, ".txt") {
		return fmt.Errorf("report file format not supported: %s", reportFileName)
	} else if match, _ := regexp.MatchString(reportFileNamePattern, reportFileName); !match {
		return fmt.Errorf("invalid report file name: %s", reportFileName)
	}
	return nil
}
