package maven

import (
	"github.com/stretchr/testify/assert"
	"os"
	"path"
	"testing"
)

func Test_NewArtifact(t *testing.T) {
	artifact, err := NewArtifact("abc:xyz:1.2.3")
	assert.NoError(t, err)
	assert.NotNil(t, artifact)
	assert.Equal(t, "abc", artifact.GroupId)
	assert.Equal(t, "xyz", artifact.ArtifactId)
	assert.Equal(t, "1.2.3", artifact.Version)
}

func Test_NewArtifact_returnsError_forInvalidCoordinates(t *testing.T) {
	artifact, err := NewArtifact("unknown")
	assert.EqualError(t, err, "invalid artifact coordinates: 'unknown'")
	assert.Nil(t, artifact)
}

func TestArtifact_Exists_CommonsIO(t *testing.T) {
	artifact, _ := NewArtifact("commons-io:commons-io:2.11.0")
	exists, err := artifact.Exists()
	assert.NoError(t, err)
	assert.True(t, exists)
}

func TestArtifact_Exists_ESAPI(t *testing.T) {
	artifact, _ := NewArtifact("org.owasp.esapi:esapi:2.2.3.1")
	exists, err := artifact.Exists()
	assert.NoError(t, err)
	assert.True(t, exists)
}

func TestArtifact_Exists_returnsFalse_forNonExistingArtifact(t *testing.T) {
	artifact, _ := NewArtifact("unknown:unknown:0.0.1")
	exists, err := artifact.Exists()
	assert.NoError(t, err)
	assert.False(t, exists)
}

func TestArtifact_GetURL(t *testing.T) {
	artifact, _ := NewArtifact("abc:xyz:1.2.3")
	url := artifact.GetURL()
	assert.Equal(t, "https://repo1.maven.org/maven2/abc/xyz/1.2.3/xyz-1.2.3.jar", url)
}

func TestArtifact_Download(t *testing.T) {
	artifact, _ := NewArtifact("commons-io:commons-io:1.0")
	tempDir := t.TempDir()
	tempFile := path.Join(tempDir, "artifact.jar")
	err := artifact.Download(tempFile)
	assert.NoError(t, err)
	fileInfo, err := os.Stat(tempFile)
	assert.Equal(t, int64(45550), fileInfo.Size())
}

func TestArtifact_Download_returnsError_forNonExistingArtifact(t *testing.T) {
	artifact, _ := NewArtifact("unknown:unknown:0.0.1")
	tempDir := t.TempDir()
	tempFile := path.Join(tempDir, "artifact.jar")
	err := artifact.Download(tempFile)
	assert.EqualError(t, err, "unexpected status code: 404")
}

func TestArtifact_ToPath(t *testing.T) {
	artifact, _ := NewArtifact("abc.def:xyz:1.2.3")
	result := artifact.ToPath()
	assert.Equal(t, "abc/def/xyz/1.2.3/xyz-1.2.3.jar", result)
}

func TestArtifact_ToCoordinates(t *testing.T) {
	artifact, _ := NewArtifact("abc.def:xyz:1.2.3")
	coordinates := artifact.ToCoordinates()
	assert.Equal(t, "abc.def:xyz:1.2.3", coordinates)
}
