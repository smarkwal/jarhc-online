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
sam local start-api --port 3001 --parameter-overrides "ApiDomain=localhost:3001 WebsiteDomain=localhost:3000 WebsiteURL=http://localhost:3000"
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
npm run dev
```

To run the production build:

```shell
npm run preview
```

### Deploy

Use `aws s3 sycn` to copy the content of the `dist/` folder into the S3 bucket `online.jarhc.org`.

Use `--delete` to delete files in S3 which do not exist locally.

Use `--exclude reports/*` to avoid deleting generated JAPICC reports.

Use `--dryrun` to display the operations that would be performed without actually running them.

```shell
aws s3 sync dist/. s3://online.jarhc.org --delete --exclude reports/* # --dryrun
```

## Project information

### Developers

* Stephan Markwalder - [@smarkwal](https://github.com/smarkwal)

### Dependencies

* [React 18](https://reactjs.org/)
* [Java 11](https://dev.java/)
* [Java API Compliance Checker (JAPICC) 2.4](https://github.com/lvc/japi-compliance-checker)
* [JarHC](https://github.com/smarkwal/jarhc)
