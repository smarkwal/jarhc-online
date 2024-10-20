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

CDK version used to create this project: `2.162.1 (build 10aa526)`

### Configuration

CDK will use the credentials configured for AWS CLI (see `~/.aws/config` and `~/.aws/credentials`).

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

| Logical ID         | Physical ID | Type                                 |
|--------------------|-------------|--------------------------------------|
| CognitoCertificate | (dynamic)   | AWS::CertificateManager::Certificate |
| WebsiteCertificate | (dynamic)   | AWS::CertificateManager::Certificate |

Copy the output values from the stack to the [env.properties](env.properties) file:

```properties
CognitoCertificateARN=arn:aws:acm:us-east-1:837783538267:certificate/4983cb68-d5f7-4862-8417-4a3a5d2aea8a
WebsiteCertificateARN=arn:aws:acm:us-east-1:837783538267:certificate/e67606ad-78b1-472a-b30d-64f406faa5e8
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

| Logical ID          | Physical ID     | Type                         |
|---------------------|-----------------|------------------------------|
| CognitoUserPool     | (dynamic)       | AWS::Cognito::UserPool       |
| CognitoAppClient    | (dynamic)       | AWS::Cognito::UserPoolClient |
| CognitoAdminGroup   | Administrators  | AWS::Cognito::UserPoolGroup  |
| CognitoCustomDomain | login.jarhc.org | AWS::Cognito::UserPoolDomain |
| CognitoDnsRecord    | login.jarhc.org | AWS::Route53::RecordSet      |

Copy the output values from the stack to the [env.properties](env.properties) file:

```properties
CognitoUserPoolARN=arn:aws:cognito-idp:eu-central-1:837783538267:userpool/eu-central-1_hGmfARjML
CognitoClientID=1lnm05s072ksriccnmohm9udmc
```

Update the React application settings in [.env](../react-app/.env):
```properties
VITE_COGNITO_CLIENT_ID=1lnm05s072ksriccnmohm9udmc
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

| Logical ID                    | Physical ID      | Type                                 |
|-------------------------------|------------------|--------------------------------------|
| WebsiteBucket                 | online.jarhc.org | AWS::S3::Bucket                      |
| WebsiteBucketPolicy           | online.jarhc.org | AWS::S3::BucketPolicy                |
| WebsiteCloudFrontDistribution | (dynamic)        | AWS::CloudFront::Distribution        |
| WebsiteOriginAccessControl    | (dynamic)        | AWS::CloudFront::OriginAccessControl |
| WebsiteDnsRecord              | (dynamic)        | AWS::Route53::RecordSetGroup         |

### Deploy the static website content (React app)

```shell
cd ../react-app
make clean build sync
```

Test the static website: https://online.jarhc.org/

### Optional: Check Cognito

Note: It may take up to an hour until the Cognito custom domain is available.

Test if the Cognito OpenID service is available: https://login.jarhc.org/.well-known/openid-configuration

### Stack 4: jarhc-online-api

