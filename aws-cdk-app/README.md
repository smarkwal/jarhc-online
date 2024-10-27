# Welcome to your CDK Java project!

This is a blank project for CDK development with Java.

The `cdk.json` file tells the CDK Toolkit how to execute your app.

It is a [Maven](https://maven.apache.org/) based project, so you can open this project with any Maven compatible Java IDE to build and run tests.

## Useful commands

* `mvn package`     compile and run tests
* `cdk ls`          list all stacks in the app
* `cdk synth`       emits the synthesized CloudFormation template
* `cdk deploy`      deploy this stack to your default AWS account/region
* `cdk diff`        compare deployed stack with current state
* `cdk docs`        open CDK documentation

Enjoy!

## Install AWS CDK

```shell
npm install -g aws-cdk
```

Verify CDK CLI version:

```shell
cdk --version
```

CDK version used to create this project: `2.164.1 (build bb9275c)`

### Configuration

CDK will use the credentials configured for AWS CLI (see `~/.aws/config` and `~/.aws/credentials`).

## Stacks

| Stack                | Description                                               | Build dependencies                         |
|----------------------|-----------------------------------------------------------|--------------------------------------------|
| jarhc-online-cert    | SSL certificates for website and Cognito (custom domains) |                                            |
| jarhc-online-cognito | Cognito user pool and app client                          | jarhc-online-cert                          |
| jarhc-online-website | S3 bucket and CloudFront distribution for website         | jarhc-online-cert, jarhc-online-cognito    |
| jarhc-online-api     | API gateway and Lambda functions                          | jarhc-online-cognito, jarhc-online-website |

## Build and deploy

Prepare AWS environment for usage with AWS CDK (only once):

```shell
cdk bootstrap
```

Create CloudFormation templates for all stacks:

```shell
cdk synth
```

### Stack 1: jarhc-online-cert

Deploy the stack with the certificates for CloudFront and Cognito in `us-east-1`:

```shell
cdk deploy jarhc-online-cert
```

Links:

* [CloudFormation (us-east-1)](https://us-east-1.console.aws.amazon.com/cloudformation/home?region=us-east-1)
* [Certificate Manager (us-east-1)](https://us-east-1.console.aws.amazon.com/acm/home?region=us-east-1)

Resources:

| Logical ID                 | Physical ID | Type                                 |
|----------------------------|-------------|--------------------------------------|
| CognitoCertificate6C6D677B | (dynamic)   | AWS::CertificateManager::Certificate |
| WebsiteCertificateEEE1FA4C | (dynamic)   | AWS::CertificateManager::Certificate |

Copy the output values from the stack to the [env.properties](env.properties) file:

```properties
CognitoCertificateARN=arn:aws:acm:us-east-1:837783538267:certificate/1439f453-fe04-46f8-9198-319b99ac9233
WebsiteCertificateARN=arn:aws:acm:us-east-1:837783538267:certificate/fb1d01cd-d534-4d22-9dab-5fd591176742
```

Regenerate the CloudFormation templates:

```shell
cdk synth
```

### Stack 2: jarhc-online-cognito

Deploy the stack for Cognito:

```shell
cdk deploy jarhc-online-cognito
```

Links:

* [CloudFormation](https://eu-central-1.console.aws.amazon.com/cloudformation/home?region=eu-central-1)
* [Cognito](https://eu-central-1.console.aws.amazon.com/cognito/v2/idp/user-pools?region=eu-central-1)

Resources:

| Logical ID                                 | Physical ID     | Type                         |
|--------------------------------------------|-----------------|------------------------------|
| CognitoUserPool53E37E69                    | (dynamic)       | AWS::Cognito::UserPool       |
| CognitoUserPoolonlinejarhcorg41904085      | (dynamic)       | AWS::Cognito::UserPoolClient |
| CognitoUserPoolCognitoCustomDomainE2E6CF17 | login.jarhc.org | AWS::Cognito::UserPoolDomain |
| CognitoDnsRecord9CDAC157                   | login.jarhc.org | AWS::Route53::RecordSet      |
| (more resource)                            | (dynamic)       | (various)                    |

Copy the output values from the stack to the [env.properties](env.properties) file:

```properties
CognitoClientID=1cc2kvdb7ip94935j2pedb9qlo
CognitoUserPoolARN=arn:aws:cognito-idp:eu-central-1:837783538267:userpool/eu-central-1_iJ5Jxf8ll
```

Update the React application settings in [.env](../react-app/.env):

```properties
VITE_COGNITO_CLIENT_ID=1cc2kvdb7ip94935j2pedb9qlo
```

Regenerate the CloudFormation templates:

```shell
cdk synth
```

### Stack 3: jarhc-online-website

Deploy the stack for the website:

```shell
cdk deploy jarhc-online-website
```

Links:

* [CloudFormation](https://eu-central-1.console.aws.amazon.com/cloudformation/home?region=eu-central-1)
* [S3](https://eu-central-1.console.aws.amazon.com/s3/buckets?region=eu-central-1)
* [CloudFront](https://us-east-1.console.aws.amazon.com/cloudfront/v4/home?region=eu-central-1)
* [Route 53](https://us-east-1.console.aws.amazon.com/route53/v2/hostedzones?region=eu-central-1)

Resources:

| Logical ID                            | Physical ID      | Type                                 |
|---------------------------------------|------------------|--------------------------------------|
| WebsiteBucket75C24D94                 | online.jarhc.org | AWS::S3::Bucket                      |
| WebsiteBucketPolicyE10E3262           | online.jarhc.org | AWS::S3::BucketPolicy                |
| WebsiteCloudFrontDistribution12928E08 | (dynamic)        | AWS::CloudFront::Distribution        |
| WebsiteOriginAccessControl57832DAD    | (dynamic)        | AWS::CloudFront::OriginAccessControl |
| WebsiteDnsRecord3059DC61              | online.jarhc.org | AWS::Route53::RecordSetGroup         |

### Deploy the static website content (React app)

```shell
cd ../react-app
make clean build sync
```

Test the static website: https://online.jarhc.org/

### Optional: Check Cognito

Note: It may take up to an hour until the Cognito custom domain is available.

Test if the Cognito OpenID service is available: https://cognito-idp.eu-central-1.amazonaws.com/eu-central-1_iJ5Jxf8ll/.well-known/openid-configuration

### Stack 4: jarhc-online-api

Build the Lambda functions:

```shell
cd ../sam-api
./gradlew clean build -x test -x jarhcReport
```

Deploy the stack for the API gateway:

```shell
cdk deploy jarhc-online-api
```

Links:

* [CloudFormation](https://eu-central-1.console.aws.amazon.com/cloudformation/home?region=eu-central-1)
* [API Gateway](https://eu-central-1.console.aws.amazon.com/apigateway/main/apis?region=eu-central-1)
* [Lambda](https://eu-central-1.console.aws.amazon.com/lambda/home?region=eu-central-1)
* [ECR](https://eu-central-1.console.aws.amazon.com/ecr/private-registry/repositories?region=eu-central-1)
* [SQS](https://eu-central-1.console.aws.amazon.com/sqs/v3/home?region=eu-central-1)
* [X-Ray](https://eu-central-1.console.aws.amazon.com/cloudwatch/home?region=eu-central-1#xray:traces)
* [Route 53](https://us-east-1.console.aws.amazon.com/route53/v2/hostedzones?region=eu-central-1)

Resources:

| Logical ID                                                | Physical ID   | Type                                 |
|-----------------------------------------------------------|---------------|--------------------------------------|
| RestApi0C43BF4B                                           | (dynamic)     | AWS::ApiGateway::RestApi             |
| RestApiDeployment180EC5030543aecfbe4b1557b12f6cbaad612eca | (dynamic)     | AWS::ApiGateway::Deployment          |
| RestApiDeploymentStageProd718AC1AC                        | (dynamic)     | AWS::ApiGateway::Stage               |
| RestApiCustomDomain91FA5E60                               | api.jarhc.org | AWS::ApiGateway::DomainName          |
| ApiDnsRecordD7F299CB                                      | api.jarhc.org | AWS::Route53::RecordSet              |
| ApiCertificate1D5B2B3B                                    | (dynamic)     | AWS::CertificateManager::Certificate |
| RestApiFunctionB9A4309E                                   | rest-api      | AWS::Lambda::Function                |
| JarhcCheckFunction1DCBD1DF                                | jarhc-check   | AWS::Lambda::Function                |
| JapiccCheckFunction77210A42                               | japicc-check  | AWS::Lambda::Function                |
| ErrorQueue2580A2D4                                        | (dynamic)     | AWS::SQS::Queue                      |
| (more resource)                                           | (dynamic)     | (various)                            |

## Cleanup

TODO:

- Delete API stack
- Empty S3 bucket with static website content
- Delete Website stack
- Delete Cognito stack
- Delete Cert stack

