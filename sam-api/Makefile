
all: validate build deploy

clean:
	@cd shared && mvn clean
	@cd japicc-check && ./gradlew clean
	rm -rf .aws-sam

validate:
	sam validate

build:
	# build and install shared library
	# (this is a workaround for SAM not supporting multi-module builds)
	@cd shared && mvn clean install -DskipTests
	# build japicc-check function
	# (artifact and dependencies will be used in Dockerfile)
	@cd japicc-check && ./gradlew clean build
	sam build

deploy:
	sam deploy

run:
	sam validate || exit 1
	sam build || exit 2
	sam local start-api --port 3001 --parameter-overrides "ApiDomain=localhost:3001 WebsiteDomain=localhost:3000 WebsiteURL=http://localhost:3000"

delete:
	sam delete
