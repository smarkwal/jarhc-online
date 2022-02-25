package main

import (
	"github.com/smarkwal/jarhc-online/rest-api/japicc"
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

	// handle potential error
	if err != nil {
		returnError(w, err)
		return
	}

	// return output of JAPICC
	w.Header().Set("Content-Type", "text/plain")
	w.Header().Set("Access-Control-Allow-Origin", "*")
	w.WriteHeader(http.StatusOK)
	w.Write(out)
}

func returnError(w http.ResponseWriter, err error) {
	returnErrorMessage(w, err.Error())
}

func returnErrorMessage(w http.ResponseWriter, message string) {
	log.Println(message)
	w.Header().Set("Content-Type", "text/plain")
	w.WriteHeader(http.StatusInternalServerError)
	w.Write([]byte(message))
}
