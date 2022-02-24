package main

import (
	"bytes"
	"github.com/gorilla/handlers"
	"github.com/gorilla/mux"
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
	r := mux.NewRouter()
	r.HandleFunc("/", rootHandler).Methods("GET")
	r.HandleFunc("/japicc/version", japiccVersionHandler).Methods("GET")

	// wrap with logging handler
	loggingHandler := handlers.LoggingHandler(os.Stdout, r)

	// start server
	log.Println("Listen on", address)
	log.Fatal(http.ListenAndServe(address, loggingHandler))
}

func rootHandler(w http.ResponseWriter, r *http.Request) {
	w.Header().Set("Content-Type", "text/plain")
	w.WriteHeader(http.StatusOK)
	w.Write([]byte("Hello from jarhc-online!\n"))
}

func japiccVersionHandler(w http.ResponseWriter, r *http.Request) {

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
