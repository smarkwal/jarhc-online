# JarHC Online

JAR Health Check online service.

# REST API

## Build and test

To build and run locally (requires Go 1.17):

```shell
cd rest-api/src
go build
./rest-api
```

To build Docker image `jarhc-online` and run container:

```shell
cd rest-api
./build-docker.sh
./run-docker.sh
```

REST API will be available here:
http://localhost:8080

# React App

## Build and test

To do.

# Project information

## Developers

* Stephan Markwalder - [@smarkwal](https://github.com/smarkwal)

## Dependencies

* [Go](https://go.dev/) version 1.17
* [gorilla/mux](https://github.com/gorilla/mux)
* [gorilla/handlers](https://github.com/gorilla/handlers)
* [Java API Compliance Checker (JAPICC)](https://github.com/lvc/japi-compliance-checker) version 2.4
