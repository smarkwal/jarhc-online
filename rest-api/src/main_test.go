package main

import (
	"github.com/stretchr/testify/assert"
	"testing"
)

func Test_sha256hex(t *testing.T) {
	result := sha256hex("abc:xyz:1.2.3")
	assert.Equal(t, "7092070915c9cffe9189fd7d6fc1a4f367ffb63fefec97867e06bcab97cb484a", result)
}
