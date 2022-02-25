package maven

import (
	"github.com/stretchr/testify/assert"
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

func Test_Exists_CommonsIO(t *testing.T) {
	artifact, _ := NewArtifact("commons-io:commons-io:2.11.0")
	exists, err := artifact.Exists()
	assert.NoError(t, err)
	assert.True(t, exists)
}

func Test_Exists_ESAPI(t *testing.T) {
	artifact, _ := NewArtifact("org.owasp.esapi:esapi:2.2.3.1")
	exists, err := artifact.Exists()
	assert.NoError(t, err)
	assert.True(t, exists)
}

func Test_Exists_returnsFalse_forNonExistingArtifact(t *testing.T) {
	artifact, _ := NewArtifact("unknown:unknown:0.0.1")
	exists, err := artifact.Exists()
	assert.NoError(t, err)
	assert.False(t, exists)
}

func Test_GetURL(t *testing.T) {
	artifact, _ := NewArtifact("abc:xyz:1.2.3")
	url := artifact.GetURL()
	assert.Equal(t, "https://repo1.maven.org/maven2/abc/xyz/1.2.3/xyz-1.2.3.jar", url)
}
