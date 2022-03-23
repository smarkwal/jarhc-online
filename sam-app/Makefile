
all: validate build deploy

validate:
	sam validate

build:
	sam build

deploy:
	sam deploy

test:
	go clean -testcache

	@echo "Testing common ..."
	@cd common && \
	 go test ./...

	@echo "Testing japicc-check ..."
	@cd japicc-check && \
	 go test ./...

	@echo "Testing rest-api ..."
	@cd rest-api && \
	 go test ./...

run:
	sam validate || exit 1
	sam build || exit 2
	sam local start-api --port 3001 --parameter-overrides "ApiDomain=localhost:3001 WebsiteDomain=localhost:3000 WebsiteURL=http://localhost:3000"

update:
	@echo "Updating common ..."
	@cd common && \
	 go mod tidy

	@echo "Updating japicc-check ..."
	@cd japicc-check && \
	 go get -u github.com/smarkwal/jarhc-online/sam-app/common@7631a7fe07e3 && \
	 go mod tidy

	@echo "Updating rest-api ..."
	@cd rest-api && \
	 go get -u github.com/smarkwal/jarhc-online/sam-app/common@7631a7fe07e3 && \
	 go mod tidy
