
all: validate build deploy

clean:
	rm -rf .aws-sam
	@cd japicc-check && mvn clean --quiet

validate:
	sam validate

build:
	@cd japicc-check && mvn clean package -DskipTests
	sam build

deploy:
	sam deploy

run:
	sam validate || exit 1
	sam build || exit 2
	sam local start-api --port 3001 --parameter-overrides "ApiDomain=localhost:3001 WebsiteDomain=localhost:3000 WebsiteURL=http://localhost:3000"

delete:
	sam delete
