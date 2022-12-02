
all: validate build deploy

validate:
	sam validate

build:
	sam build

deploy:
	sam deploy

test:
	go clean -testcache

	@echo "Testing japicc-check ..."
	@cd japicc-check && \
	 go test ./...

run:
	sam validate || exit 1
	sam build || exit 2
	sam local start-api --port 3001 --parameter-overrides "ApiDomain=localhost:3001 WebsiteDomain=localhost:3000 WebsiteURL=http://localhost:3000"

update:

	@echo "Updating japicc-check ..."
	@cd japicc-check && \
	 go mod tidy
