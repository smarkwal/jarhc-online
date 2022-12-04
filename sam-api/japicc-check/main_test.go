package main

import (
	"github.com/stretchr/testify/assert"
	"testing"
)

func Test_validateReportFileName(t *testing.T) {
	reportFileName := "report-5d75918f818d3baf3565a9ac506b4eb4d08b2e7341f979d1bc99c06ab8e2ab1d.html"
	err := validateReportFileName(reportFileName)
	assert.NoError(t, err)
}
