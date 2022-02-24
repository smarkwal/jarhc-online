package main

import (
	"github.com/gorilla/handlers"
	"github.com/gorilla/mux"
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
	r := mux.NewRouter()
	r.HandleFunc("/", rootHandler).Methods("GET")

	// wrap with logging handler
	handler := handlers.LoggingHandler(os.Stdout, r)

	// start server
	log.Println("Listen on", address)
	log.Fatal(http.ListenAndServe(address, handler))
}

func rootHandler(w http.ResponseWriter, r *http.Request) {
	w.Header().Set("Content-Type", "text/plain")
	w.WriteHeader(http.StatusOK)
	w.Write([]byte("Hello from jarhc-online!\n"))
}
