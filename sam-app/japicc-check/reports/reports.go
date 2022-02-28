package reports

import (
	"fmt"
	"github.com/aws/aws-sdk-go/aws"
	"github.com/aws/aws-sdk-go/aws/awserr"
	"github.com/aws/aws-sdk-go/aws/session"
	"github.com/aws/aws-sdk-go/service/s3"
	"github.com/aws/aws-sdk-go/service/s3/s3manager"
	"io"
	"net/url"
)

const NotFound = "NotFound"

type S3Client struct {
	Region     string
	BucketName string
}

func NewS3Client(region string, bucketName string) S3Client {
	return S3Client{
		Region:     region,
		BucketName: bucketName,
	}
}

func (c *S3Client) GetURL(fileName string) string {
	return "http://online.jarhc.org/reports/" + url.PathEscape(fileName)
}

func (c *S3Client) Exists(fileName string) (bool, error) {

	// connect to S3
	config := aws.Config{Region: aws.String(c.Region)}
	sess, err := session.NewSession(&config)
	if err != nil {
		fmt.Println("Error:", err)
		return false, err
	}
	service := s3.New(sess)

	// check if object exists
	input := &s3.HeadObjectInput{
		Bucket: aws.String(c.BucketName),
		Key:    aws.String("reports/" + fileName),
	}
	fmt.Println("Input:", input)

	output, err := service.HeadObject(input)

	// handle potential error
	if err != nil {
		fmt.Println("Error:", err)
		err2, ok := err.(awserr.Error)
		if ok {
			switch err2.Code() {
			case NotFound: // s3.ErrCodeNoSuchKey does not work
				return false, nil
			default:
				return false, err
			}
		}
		return false, err
	}

	fmt.Println("Output:", output)
	return true, nil
}

func (c *S3Client) Upload(fileName string, file io.Reader) (bool, error) {

	// connect to S3
	config := aws.Config{Region: aws.String(c.Region)}
	sess, err := session.NewSession(&config)
	if err != nil {
		fmt.Println("Error:", err)
		return false, err
	}

	uploader := s3manager.NewUploader(sess)

	input := &s3manager.UploadInput{
		Bucket:      aws.String(c.BucketName),
		Key:         aws.String("reports/" + fileName),
		Body:        file,
		ContentType: aws.String("text/html"),
	}
	fmt.Println("Input:", input)

	output, err := uploader.Upload(input)
	if err != nil {
		fmt.Println("Error:", err)
		return false, err
	}

	fmt.Println("Output:", output)
	return true, nil
}

func (c *S3Client) Delete(fileName string) (bool, error) {

	// connect to S3
	config := aws.Config{Region: aws.String(c.Region)}
	sess, err := session.NewSession(&config)
	if err != nil {
		fmt.Println("Error:", err)
		return false, err
	}
	service := s3.New(sess)

	input := s3.DeleteObjectInput{
		Bucket: aws.String(c.BucketName),
		Key:    aws.String("reports/" + fileName),
	}
	fmt.Println("Input:", input)

	output, err := service.DeleteObject(&input)
	if err != nil {
		fmt.Println("Error:", err)
		return false, err
	}

	fmt.Println("Output:", output)
	return true, nil
}
