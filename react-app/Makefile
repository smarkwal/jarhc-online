.PHONY: build

all: clean build sync

clean:
	rm -rf dist

outdated:
	npm outdated

build:
	npm run build

run-dev: clean
	npm run dev

run-prod: clean build
	npm run preview

sync:
	# see https://docs.aws.amazon.com/cli/latest/reference/s3/sync.html
	aws s3 sync dist/. s3://online.jarhc.org --delete --exclude reports/*

delete:
	aws s3 rm s3://online.jarhc.org --recursive
