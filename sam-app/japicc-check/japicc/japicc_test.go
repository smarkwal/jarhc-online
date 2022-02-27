package japicc

import (
	"github.com/stretchr/testify/assert"
	"testing"
)

func Test_IsInstalled(t *testing.T) {
	result := IsInstalled()
	assert.False(t, result)
}

func Test_GetVersion(t *testing.T) {
	out, err := GetVersion()
	assert.Errorf(t, err, "JAPICC is not installed.")
	assert.Nil(t, out)
}
