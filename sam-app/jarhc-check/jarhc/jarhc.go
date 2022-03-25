package jarhc

import (
	"bytes"
	"log"
	"os/exec"
)

type Options struct {
	Classpath []string `json:"classpath"`
	Provided  []string `json:"provided"`
}

func Check(options Options, reportFilePath string) error {

	// prepare arguments
	args := []string{
		"-jar", "jarhc.jar",
		"--output", reportFilePath,
		"--data", "/tmp/jarhc",
	}

	// add all classpath artifacts
	for _, artifact := range options.Classpath {
		args = append(args, "--classpath", artifact)
	}

	// add all provided artifacts
	for _, artifact := range options.Provided {
		args = append(args, "--provided", artifact)
	}

	// run Java application
	_, err := runCommand("java", args...)
	if err != nil {
		return err
	}

	return nil
}

// runCommand Run JarHC with given arguments.
func runCommand(binary string, args ...string) ([]byte, error) {

	// prepare JarHC command
	cmd := exec.Command(binary, args...)

	// buffer output sent to STDOUT and STDERR
	var out bytes.Buffer
	cmd.Stdout = &out
	cmd.Stderr = &out

	// execute JAPICC command
	log.Println("Run JarHC command:", binary, args)
	err := cmd.Run()

	// log command output
	data := out.Bytes()
	log.Printf("Output:\n%s\n", string(data))

	return data, err
}
