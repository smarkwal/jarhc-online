package main

import (
	"github.com/stretchr/testify/assert"
	"testing"
)

func Test_isValidVersion(t *testing.T) {

	result := isValidVersion("")
	assert.False(t, result)

	result = isValidVersion("unknown")
	assert.False(t, result)

	result = isValidVersion("junit-5.0.jar")
	assert.False(t, result)

	result = isValidVersion("group:artifact:")
	assert.False(t, result)

	result = isValidVersion("group::version")
	assert.False(t, result)

	result = isValidVersion(":artifact:version")
	assert.False(t, result)

	result = isValidVersion("g:a:v")
	assert.True(t, result)

	result = isValidVersion("abc:xyz:1.2.3")
	assert.True(t, result)

	result = isValidVersion("commons-io:commons-io:2.10.0")
	assert.True(t, result)

	result = isValidVersion("org.springframework:spring-core:5.3.0")
	assert.True(t, result)

	result = isValidVersion("org.springframework:spring-core:5.3.0:pom")
	assert.False(t, result)

}
