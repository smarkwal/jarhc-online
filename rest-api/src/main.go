package main

import (
	"crypto/sha256"
	"encoding/json"
	"fmt"
	"github.com/smarkwal/jarhc-online/rest-api/japicc"
	"github.com/smarkwal/jarhc-online/rest-api/maven"
	"log"
	"net/http"
	"net/url"
	"os"
	"path"
	"regexp"
	"strings"
)

var tempDirPath = path.Join(os.TempDir(), "jarhc-online")

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
	router.HandleFunc("/japicc/check", japiccCheckHandler)
	router.HandleFunc("/japicc/report/", japiccReportHandler)
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

func rootHandler(w http.ResponseWriter, _ *http.Request) {
	// TODO: only support GET requests

	w.Header().Set("Content-Type", "text/plain")
	w.WriteHeader(http.StatusOK)
	_, err := w.Write([]byte("Hello from jarhc-online!\n"))
	if err != nil {
		log.Println(err)
	}
}

func japiccVersionHandler(w http.ResponseWriter, _ *http.Request) {
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
	_, err = w.Write(out)
	if err != nil {
		log.Println(err)
	}
}

type JapiccCheckRequest struct {
	OldVersion string `json:"oldVersion"`
	NewVersion string `json:"newVersion"`
}

type JapiccCheckResponse struct {
	ReportURL    string `json:"reportURL"`
	ErrorMessage string `json:"errorMessage"`
}

func japiccCheckHandler(w http.ResponseWriter, r *http.Request) {

	// handle CORS preflight request
	if r.Method == "OPTIONS" {
		w.Header().Set("Access-Control-Allow-Origin", "*")
		w.Header().Set("Access-Control-Allow-Methods", "POST")
		w.Header().Set("Access-Control-Allow-Headers", "Content-Type")
		w.Header().Set("Access-Control-Max-Age", "86400")
		w.WriteHeader(http.StatusNoContent)
		return
	}

	// TODO: only support POST requests

	// parse JSON request body
	var request JapiccCheckRequest
	err := json.NewDecoder(r.Body).Decode(&request)
	if err != nil {
		returnError(w, http.StatusBadRequest, err)
		return
	}

	oldVersion := request.OldVersion
	log.Println("old version:", oldVersion)

	newVersion := request.NewVersion
	log.Println("old version:", newVersion)

	// calculate hash for combination of versions
	hash := sha256hex(oldVersion + "/" + newVersion)

	// prepare report file
	reportFileName := "report-" + hash + ".html"
	reportFilePath := path.Join(tempDirPath, "reports", reportFileName)
	log.Println("report file path:", reportFilePath)

	// check if report file already exists
	info, err := os.Stat(reportFilePath)
	if err == nil && info != nil && !info.IsDir() {
		log.Println("report file size: ", getFileSize(reportFilePath))

		returnReportFile(w, reportFileName, err)
		return
	}

	err = os.MkdirAll(path.Dir(reportFilePath), os.ModePerm)
	if err != nil {
		returnError(w, http.StatusInternalServerError, err)
		return
	}

	oldArtifact, err := maven.NewArtifact(oldVersion)
	if err != nil {
		returnError(w, http.StatusBadRequest, err)
		return
	}
	log.Println("old artifact:", oldArtifact.ToCoordinates())

	newArtifact, err := maven.NewArtifact(newVersion)
	if err != nil {
		returnError(w, http.StatusBadRequest, err)
		return
	}
	log.Println("new artifact:", newArtifact.ToCoordinates())

	oldFilePath := path.Join(tempDirPath, oldArtifact.ToPath())
	log.Println("old file path:", oldFilePath)
	err = os.MkdirAll(path.Dir(oldFilePath), os.ModePerm)
	if err != nil {
		returnError(w, http.StatusInternalServerError, err)
		return
	}
	err = oldArtifact.Download(oldFilePath)
	if err != nil {
		returnError(w, http.StatusInternalServerError, err)
		return
	}
	log.Println("old file size: ", getFileSize(oldFilePath))

	newFilePath := path.Join(tempDirPath, newArtifact.ToPath())
	log.Println("new file path:", newFilePath)
	err = os.MkdirAll(path.Dir(newFilePath), os.ModePerm)
	if err != nil {
		returnError(w, http.StatusInternalServerError, err)
		return
	}
	err = newArtifact.Download(newFilePath)
	if err != nil {
		returnError(w, http.StatusInternalServerError, err)
		return
	}
	log.Println("new file size: ", getFileSize(newFilePath))

	err = japicc.Check(oldFilePath, newFilePath, reportFilePath)
	if err != nil {
		returnError(w, http.StatusInternalServerError, err)
		return
	}
	log.Println("report file size: ", getFileSize(reportFilePath))

	returnReportFile(w, reportFileName, err)
}

func returnReportFile(w http.ResponseWriter, reportFileName string, err error) {

	// prepare JSON response
	var response JapiccCheckResponse
	if err == nil {
		response.ReportURL = "http://localhost:8080/japicc/report/" + url.PathEscape(reportFileName)
	} else {
		response.ErrorMessage = err.Error()
	}

	// send response to client
	w.Header().Set("Content-Type", "application/json")
	w.Header().Set("Access-Control-Allow-Origin", "*")
	w.WriteHeader(http.StatusOK)
	err = json.NewEncoder(w).Encode(response)
	if err != nil {
		log.Println(err)
	}
	return
}

func japiccReportHandler(w http.ResponseWriter, r *http.Request) {
	// TODO: only support GET requests

	// get report file name from URL path
	reportFileName := strings.TrimPrefix(r.URL.Path, "/japicc/report/")

	// check file name
	matched, err := regexp.MatchString("^report-[0-9a-f\\-]{64}.html$", reportFileName)
	if err != nil {
		returnError(w, http.StatusInternalServerError, err)
		return
	} else if !matched {
		returnErrorMessage(w, http.StatusBadRequest, "invalid path")
		return
	}

	// get path to report file
	reportFilePath := path.Join(tempDirPath, "reports", reportFileName)

	// TODO: use html.ServeFile(...)?

	// read report file
	data, err := os.ReadFile(reportFilePath)
	if err != nil {
		returnErrorMessage(w, http.StatusNotFound, "report not found")
		return
	}

	// send report to client
	w.Header().Set("Content-Type", "text/html")
	w.WriteHeader(http.StatusOK)
	_, err = w.Write(data)
	if err != nil {
		log.Println(err)
	}
}

func mavenSearchHandler(w http.ResponseWriter, r *http.Request) {
	// TODO: only support GET requests

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

	// prepare list of artifacts
	//goland:noinspection ALL
	artifacts := []maven.Artifact{}
	if exists {
		artifacts = append(artifacts, *artifact)
	}

	// return artifacts as JSON array
	w.Header().Set("Content-Type", "application/json")
	w.Header().Set("Access-Control-Allow-Origin", "*")
	w.WriteHeader(http.StatusOK)
	err = json.NewEncoder(w).Encode(artifacts)
	if err != nil {
		log.Println(err)
	}
}

func returnError(w http.ResponseWriter, statusCode int, err error) {
	returnErrorMessage(w, statusCode, err.Error())
}

func returnErrorMessage(w http.ResponseWriter, statusCode int, message string) {
	log.Println(message)

	// send error message to client
	w.Header().Set("Content-Type", "text/plain")
	w.Header().Set("Access-Control-Allow-Origin", "*")
	http.Error(w, message, statusCode)
}

func getFileSize(path string) int64 {
	info, err := os.Stat(path)
	if err != nil {
		return -1
	}
	return info.Size()
}

func sha256hex(text string) string {
	data := []byte(text)
	hash := sha256.Sum256(data)
	return fmt.Sprintf("%x", hash[:])
}
