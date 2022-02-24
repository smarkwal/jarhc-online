package main

import (
	"bytes"
	"log"
	"net/http"
	"os"
	"os/exec"
)

func main() {
	log.Println("Start server...")

	// get TCP port to use
	port := os.Getenv("PORT")
	if len(port) == 0 {
		port = "8080"
	}

	// create inet address to listen
	address := ":" + port

	// set up router
	router := http.NewServeMux()
	router.HandleFunc("/", rootHandler)
	router.HandleFunc("/japicc/version", japiccVersionHandler)

	// wrap with logging handler
	loggingHandler := newLoggingHandler(router)

	// start server
	log.Println("Listen on", address)
	log.Fatal(http.ListenAndServe(address, loggingHandler))
}

func newLoggingHandler(next http.Handler) http.Handler {
	return http.HandlerFunc(
		func(w http.ResponseWriter, r *http.Request) {
			// TODO: capture and log status code
			log.Println(r.Method, r.URL)
			next.ServeHTTP(w, r)
		},
	)
}

func rootHandler(w http.ResponseWriter, r *http.Request) {
	// TODO: only support GET requests

	w.Header().Set("Content-Type", "text/plain")
	w.WriteHeader(http.StatusOK)
	w.Write([]byte("Hello from jarhc-online!\n"))
}

func japiccVersionHandler(w http.ResponseWriter, r *http.Request) {
	// TODO: only support GET requests

	// prepare JAPICC command
	cmd := exec.Command("/usr/bin/japi-compliance-checker", "--version")

	// buffer output sent to STDOUT and STDERR
	var out bytes.Buffer
	cmd.Stdout = &out
	cmd.Stderr = &out

	// execute JAPICC command
	err := cmd.Run()

	// handle potential error
	if err != nil {
		returnError(w, err)
		return
	}

	// return output of JAPICC
	w.Header().Set("Content-Type", "text/plain")
	w.WriteHeader(http.StatusOK)
	w.Write(out.Bytes())
}

func returnError(w http.ResponseWriter, err error) {
	log.Println(err)
	w.Header().Set("Content-Type", "text/plain")
	w.WriteHeader(http.StatusInternalServerError)
	w.Write([]byte(err.Error()))
}
