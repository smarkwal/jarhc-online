#!/bin/bash

go clean -testcache

cd japicc-check || exit 1
go test ./...

cd ../maven-search || exit 2
go test ./...
