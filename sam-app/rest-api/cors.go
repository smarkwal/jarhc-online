package main

import "os"

func addCorsHeaders(headers map[string]string) {
	var websiteUrl = os.Getenv("WEBSITE_URL")
	if len(websiteUrl) == 0 {
		websiteUrl = "http://localhost:3000"
	}
	headers["Access-Control-Allow-Origin"] = websiteUrl
	headers["Access-Control-Allow-Credentials"] = "true"
}
