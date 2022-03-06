
all: validate build deploy

validate:
	sam validate

build:
	sam build

deploy:
	sam deploy

test:
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

run:
	sam validate || exit 1
	sam build || exit 2
	sam local start-api --port 3001 --parameter-overrides "ApiDomain=localhost:3001 WebsiteDomain=localhost:3000 WebsiteURL=http://localhost:3000"

update:
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
