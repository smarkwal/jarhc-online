package japicc

import (
	"bytes"
	"errors"
	"os"
	"os/exec"
)

// path to JAPICC command on local system
const binary = "/usr/bin/japi-compliance-checker"

// IsInstalled Check if JAPICC is installed.
func IsInstalled() bool {
	_, err := os.Stat(binary)
	if err == nil {
		return true
	}
	return false
}

// GetVersion Run JAPICC with option '--version' and return the output.
func GetVersion() ([]byte, error) {
	return runCommand("--version")
}

// runCommand Run JAPICC with given arguments.
func runCommand(args ...string) ([]byte, error) {

	// check if JAPICC is installed
	if !IsInstalled() {
		return nil, errors.New("JAPICC is not installed.")
	}

	// prepare JAPICC command
	cmd := exec.Command(binary, args...)

	// buffer output sent to STDOUT and STDERR
	var out bytes.Buffer
	cmd.Stdout = &out
	cmd.Stderr = &out

	// execute JAPICC command
	err := cmd.Run()

	return out.Bytes(), err
}
