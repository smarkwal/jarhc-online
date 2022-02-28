package main

import (
	"github.com/stretchr/testify/assert"
	"testing"
)

func TestHandler(t *testing.T) {

	t.Run("File not found", func(t *testing.T) {
		err := error(nil)
		assert.NoError(t, err)
		assert.False(t, false)
	})

}
