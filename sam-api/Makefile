
all: validate build deploy

clean:
	./gradlew clean

validate:
	./gradlew test
	sam validate

build:
	# build japicc-check function
	# (artifact and dependencies will be used in Dockerfile)
	@cd japicc-check && ../gradlew build -x test
	sam build

deploy:
	sam deploy

run: validate build
	sam local start-api --port 3001 --parameter-overrides "ApiDomain=localhost:3001 WebsiteDomain=localhost:3000 WebsiteURL=http://localhost:3000"

delete:
	sam delete
