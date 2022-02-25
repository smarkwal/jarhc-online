package main

import (
	"encoding/json"
	"github.com/smarkwal/jarhc-online/rest-api/japicc"
	"github.com/smarkwal/jarhc-online/rest-api/maven"
	"log"
	"net/http"
	"os"
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
	router.HandleFunc("/maven/search", mavenSearchHandler)

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

	// get JAPICC version
	out, err := japicc.GetVersion()
	if err != nil {
		returnError(w, http.StatusInternalServerError, err)
		return
	}

	// return output of JAPICC
	w.Header().Set("Content-Type", "text/plain")
	w.Header().Set("Access-Control-Allow-Origin", "*")
	w.WriteHeader(http.StatusOK)
	w.Write(out)
}

func mavenSearchHandler(w http.ResponseWriter, r *http.Request) {

	// get coordinates from query string
	query := r.URL.Query()
	coordinates := query.Get("coordinates")
	if len(coordinates) == 0 {
		returnErrorMessage(w, http.StatusBadRequest, "missing parameter: 'coordinates'")
		return
	}

	// parse coordinates
	artifact, err := maven.NewArtifact(coordinates)
	if err != nil {
		returnError(w, http.StatusBadRequest, err)
		return
	}

	// check if artifact exists
	exists, err := artifact.Exists()
	if err != nil {
		returnError(w, http.StatusInternalServerError, err)
		return
	}
	if !exists {
		returnErrorMessage(w, http.StatusNotFound, "artifact not found")
		return
	}

	// return artifact
	w.Header().Set("Content-Type", "application/json")
	w.Header().Set("Access-Control-Allow-Origin", "*")
	w.WriteHeader(http.StatusOK)
	json.NewEncoder(w).Encode(artifact)
}

func returnError(w http.ResponseWriter, statusCode int, err error) {
	returnErrorMessage(w, statusCode, err.Error())
}

func returnErrorMessage(w http.ResponseWriter, statusCode int, message string) {
	log.Println(message)

	w.Header().Set("Content-Type", "text/plain")
	w.Header().Set("Access-Control-Allow-Origin", "*")
	w.WriteHeader(statusCode)
	w.Write([]byte(message))
}
