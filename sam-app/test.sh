#!/bin/bash

go clean -testcache

echo "Testing common ..."
cd common || exit 1
go test ./...

echo "Testing japicc-check ..."
cd ../japicc-check || exit 1
go test ./...

echo "Testing japicc-submit ..."
cd ../japicc-submit || exit 1
go test ./...

echo "Testing maven-search ..."
cd ../maven-search || exit 1
go test ./...
