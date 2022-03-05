#!/bin/bash

echo "Updating common ..."
cd common || exit 1
go mod tidy

echo "Updating japicc-check ..."
cd ../japicc-check || exit 1
go get -u github.com/smarkwal/jarhc-online/sam-app/common@bef2b82521144958a1a95163d01ee1fbdb018bd8
go mod tidy

echo "Updating japicc-submit ..."
cd ../japicc-submit || exit 1
go get -u github.com/smarkwal/jarhc-online/sam-app/common@bef2b82521144958a1a95163d01ee1fbdb018bd8
go mod tidy

echo "Updating maven-search ..."
cd ../maven-search || exit 1
go get -u github.com/smarkwal/jarhc-online/sam-app/common@bef2b82521144958a1a95163d01ee1fbdb018bd8
go mod tidy
