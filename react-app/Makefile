.PHONY: build

all: clean build sync

clean:
	rm -rf build

build:
	npm run build

sync:
	# see https://docs.aws.amazon.com/cli/latest/reference/s3/sync.html
	aws s3 sync build/. s3://online.jarhc.org --delete --exclude reports/*

delete:
	aws s3 rm s3://online.jarhc.org --recursive

run:
	npm start
