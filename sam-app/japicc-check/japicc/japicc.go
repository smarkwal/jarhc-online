package japicc

import (
	"bytes"
	"errors"
	"log"
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

func Check(oldFilePath string, newFilePath string, reportFilePath string) error {

	_, err := runCommand("-old", oldFilePath, "-new", newFilePath, "-report-path", reportFilePath)
	if err != nil {
		var exitError *exec.ExitError
		if errors.As(err, &exitError) {
			if exitError.ExitCode() == 1 {
				// incompatibilities found
			} else {
				return err
			}
		} else {
			return err
		}
	}

	return nil
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
	log.Println("Run JAPICC command:", args)
	err := cmd.Run()

	// log command output
	data := out.Bytes()
	log.Println("Output:\n", string(data))

	return data, err
}
