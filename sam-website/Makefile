.PHONY: build

all: validate build deploy

clean:
	rm -rf .aws-sam

validate:
	sam validate

build:
	sam build

deploy:
	sam deploy

delete:
	aws s3 rm s3://online.jarhc.org --recursive
	sam delete
