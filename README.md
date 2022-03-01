# JarHC Online

JAR Health Check online service.

## Serverless API

### Build

```shell
sam validate
sam build
```

### Run

```shell
sam local start-api --port 8080 --parameter-overrides "ApiDomain=localhost:8080 WebsiteDomain=localhost:3000 WebsiteURL=http://localhost:3000"
```

### Deploy

```shell
sam deploy
```

## React App

### Build

```shell
npm run build
```

### Run

To run during development:

```shell
npm start
```

To run the production build:

```shell
npm install -g serve
serve -s build
```

### Deploy

Use `aws s3 sycn` to copy the content of the `build/` folder into the S3 bucket `online.jarhc.org`.

Use `--delete` to delete files in S3 which do not exist locally.

Use `--exclude reports/*` to avoid deleting generated JAPICC reports.

Use `--dryrun` to display the operations that would be performed without actually running them.

```shell
aws s3 sync build/. s3://online.jarhc.org --delete --exclude reports/* # --dryrun
```

## Project information

### Developers

* Stephan Markwalder - [@smarkwal](https://github.com/smarkwal)

### Dependencies

* [Go](https://go.dev/) version 1.17
* [Java API Compliance Checker (JAPICC)](https://github.com/lvc/japi-compliance-checker) version 2.4
