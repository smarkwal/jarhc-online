package japicc

import (
	"github.com/smarkwal/jarhc-online/sam-app/common/maven"
	"github.com/stretchr/testify/assert"
	"os"
	"path"
	"strings"
	"testing"
)

func Test_IsInstalled(t *testing.T) {
	result := IsInstalled()
	assert.True(t, result)
}

func Test_GetVersion(t *testing.T) {
	out, err := GetVersion()
	assert.NoError(t, err)
	assert.NotNil(t, out)
	assert.True(t, strings.HasPrefix(string(out), "Java API Compliance Checker (JAPICC) 2.4"))
}

func Test_Check(t *testing.T) {

	// get commons-io-2.10.0.jar from Maven Central
	oldArtifact, err := maven.NewArtifact("commons-io:commons-io:2.10.0")
	assert.NoError(t, err)
	oldFilePath := path.Join(t.TempDir(), "commons-io-2.10.0.jar")
	err = oldArtifact.Download(oldFilePath)
	assert.NoError(t, err)

	// get commons-io-2.11.0.jar from Maven Central
	newArtifact, err := maven.NewArtifact("commons-io:commons-io:2.11.0")
	assert.NoError(t, err)
	newFilePath := path.Join(t.TempDir(), "commons-io-2.11.0.jar")
	err = newArtifact.Download(newFilePath)
	assert.NoError(t, err)

	// prepare report path
	reportFilePath := path.Join(t.TempDir(), "report.html")

	// test
	err = Check(oldFilePath, newFilePath, reportFilePath)

	// assert
	assert.NoError(t, err)
	info, err := os.Stat(reportFilePath)
	assert.NoError(t, err)
	assert.NotNil(t, info)
	assert.True(t, info.Size() > 1024)

}
