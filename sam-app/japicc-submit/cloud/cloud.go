package cloud

import (
	"fmt"
	"github.com/aws/aws-sdk-go/aws"
	"github.com/aws/aws-sdk-go/aws/awserr"
	"github.com/aws/aws-sdk-go/aws/session"
	"github.com/aws/aws-sdk-go/service/s3"
	"github.com/aws/aws-sdk-go/service/s3/s3manager"
	"github.com/aws/aws-sdk-go/service/sqs"
	"io"
	"log"
	"net/url"
	"os"
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
	var bucketUrl = os.Getenv("BUCKET_URL")
	if len(bucketUrl) == 0 {
		bucketUrl = "http://localhost:3000"
	}
	return bucketUrl + "/reports/" + url.PathEscape(fileName)
}

func (c *S3Client) Exists(fileName string) (bool, error) {

	// connect to S3
	config := aws.Config{
		Region: aws.String(c.Region),
	}
	sess, err := session.NewSession(&config)
	if err != nil {
		fmt.Println("AWS error:", err)
		return false, err
	}
	service := s3.New(sess)

	// check if object exists
	input := s3.HeadObjectInput{
		Bucket: aws.String(c.BucketName),
		Key:    aws.String("reports/" + fileName),
	}
	fmt.Println("S3 input:", input)

	output, err := service.HeadObject(&input)

	// handle potential error
	if err != nil {
		err2, ok := err.(awserr.Error)
		if ok {
			switch err2.Code() {
			case NotFound: // s3.ErrCodeNoSuchKey does not work
				fmt.Println("S3 response:", err)
				return false, nil
			default:
				fmt.Println("S3 error:", err)
				return false, err
			}
		}
		fmt.Println("S3 error:", err)
		return false, err
	}

	fmt.Println("S3 output:", *output)
	return true, nil
}

func (c *S3Client) Upload(fileName string, file io.Reader) (bool, error) {

	// connect to S3
	config := aws.Config{Region: aws.String(c.Region)}
	sess, err := session.NewSession(&config)
	if err != nil {
		fmt.Println("AWS error:", err)
		return false, err
	}

	uploader := s3manager.NewUploader(sess)

	input := s3manager.UploadInput{
		Bucket:      aws.String(c.BucketName),
		Key:         aws.String("reports/" + fileName),
		Body:        file,
		ContentType: aws.String("text/html"),
	}
	fmt.Println("S3 input:", input)

	output, err := uploader.Upload(&input)
	if err != nil {
		fmt.Println("S3 error:", err)
		return false, err
	}

	fmt.Println("S3 output:", *output)
	return true, nil
}

func (c *S3Client) Delete(fileName string) (bool, error) {

	// connect to S3
	config := aws.Config{Region: aws.String(c.Region)}
	sess, err := session.NewSession(&config)
	if err != nil {
		fmt.Println("AWS error:", err)
		return false, err
	}
	service := s3.New(sess)

	input := s3.DeleteObjectInput{
		Bucket: aws.String(c.BucketName),
		Key:    aws.String("reports/" + fileName),
	}
	fmt.Println("S3 input:", input)

	output, err := service.DeleteObject(&input)
	if err != nil {
		fmt.Println("S3 error:", err)
		return false, err
	}

	fmt.Println("S3 output:", *output)
	return true, nil
}

// SQS =========================================================================

type SQSClient struct {
	Region     string
	connection *sqs.SQS
}

func NewSQSClient(region string) (*SQSClient, error) {

	// connect to SQS
	config := aws.Config{Region: aws.String(region)}
	sess, err := session.NewSession(&config)
	if err != nil {
		fmt.Println("AWS error:", err)
		return nil, err
	}
	connection := sqs.New(sess)

	// return client
	client := SQSClient{
		Region:     region,
		connection: connection,
	}
	return &client, nil
}

func (c *SQSClient) SendMessage(queueName string, payload string) (*string, error) {

	// get SQS queue URL
	queueURL, err := c.getQueueURL(queueName)
	if err != nil {
		return nil, err
	}

	// send message to SQS queue
	input := sqs.SendMessageInput{
		QueueUrl:    queueURL,
		MessageBody: aws.String(payload),
	}
	log.Println("SQS input:", input)
	output, err := c.connection.SendMessage(&input)
	if err != nil {
		log.Println("SQS error:", err)
		return nil, err
	}
	log.Println("SQS output:", *output)

	// return message ID
	messageId := output.MessageId
	return messageId, nil
}

func (c *SQSClient) getQueueURL(queueName string) (*string, error) {
	log.Println("get queue URL:", queueName)

	input := sqs.GetQueueUrlInput{
		QueueName: aws.String(queueName),
	}
	log.Println("SQS input:", input)
	output, err := c.connection.GetQueueUrl(&input)
	if err != nil {
		log.Println("SQS error:", err)
		return nil, err
	}
	log.Println("SQS output:", *output)

	return output.QueueUrl, nil
}

func (c *SQSClient) DeleteMessage(queueName string, messageId string) error {
	// TODO: implement
	return nil
}