package cloud

import (
	"github.com/stretchr/testify/assert"
	"strings"
	"testing"
	"time"
)

const region = "eu-central-1"
const bucketName = "online.jarhc.org"

func TestHandler(t *testing.T) {

	// create unique file name
	fileName := "test-" + time.Now().Format("20060102-150405") + ".txt"

	// prepare S3 client
	s3 := NewS3Client(region, bucketName)

	t.Run("File not found", func(t *testing.T) {
		exists, err := s3.Exists(fileName)
		assert.NoError(t, err)
		assert.False(t, exists)
	})

	t.Run("Upload file", func(t *testing.T) {
		reader := strings.NewReader("hello world")
		exists, err := s3.Upload(fileName, reader)
		assert.NoError(t, err)
		assert.True(t, exists)
	})

	t.Run("File exists", func(t *testing.T) {
		exists, err := s3.Exists(fileName)
		assert.NoError(t, err)
		assert.True(t, exists)
	})

	t.Run("Delete file", func(t *testing.T) {
		exists, err := s3.Delete(fileName)
		assert.NoError(t, err)
		assert.True(t, exists)
	})

	t.Run("File deleted", func(t *testing.T) {
		exists, err := s3.Exists(fileName)
		assert.NoError(t, err)
		assert.False(t, exists)
	})

}
