package com.myorg;

import software.constructs.Construct;

import java.util.*;
import software.amazon.awscdk.CfnMapping;
import software.amazon.awscdk.CfnTag;
import software.amazon.awscdk.Stack;
import software.amazon.awscdk.StackProps;

import software.amazon.awscdk.*;
import software.amazon.awscdk.services.apigateway.*;
import software.amazon.awscdk.services.certificatemanager.*;
import software.amazon.awscdk.services.iam.*;
import software.amazon.awscdk.services.lambda.*;
import software.amazon.awscdk.services.route53.*;
import software.amazon.awscdk.services.sqs.*;

class JarhcOnlineApiStack extends Stack {
    private Object apiGatewayUrl;

    public Object getApiGatewayUrl() {
        return this.apiGatewayUrl;
    }

    public JarhcOnlineApiStack(final Construct scope, final String id) {
        super(scope, id, null);
    }

    public JarhcOnlineApiStack(final Construct scope, final String id, final StackProps props) {
        this(scope, id, props, null, null, null, null, null, null);
    }

    public JarhcOnlineApiStack(final Construct scope, final String id, final StackProps props,
            String dnsZoneId,
            String websiteBucketName,
            String websiteUrl,
            String cognitoUserPoolArn,
            String websiteBucketUrl,
            String apiDomain) {
        super(scope, id, props);

        dnsZoneId = Optional.ofNullable(dnsZoneId).isPresent() ? dnsZoneId
                : "Z39VOOPW73P7H0";
        websiteBucketName = Optional.ofNullable(websiteBucketName).isPresent() ? websiteBucketName
                : "online.jarhc.org";
        websiteUrl = Optional.ofNullable(websiteUrl).isPresent() ? websiteUrl
                : "https://online.jarhc.org";
        cognitoUserPoolArn = Optional.ofNullable(cognitoUserPoolArn).isPresent() ? cognitoUserPoolArn
                : "arn:aws:cognito-idp:eu-central-1:837783538267:userpool/eu-central-1_duUV8DgO7";
        websiteBucketUrl = Optional.ofNullable(websiteBucketUrl).isPresent() ? websiteBucketUrl
                : "https://online.jarhc.org";
        apiDomain = Optional.ofNullable(apiDomain).isPresent() ? apiDomain
                : "api.jarhc.org";

        CfnCertificate apiCertificate = CfnCertificate.Builder.create(this, "ApiCertificate")
                .domainName(apiDomain)
                .validationMethod("DNS")
                .domainValidationOptions(Arrays.asList(
                        CfnCertificate.DomainValidationOptionProperty.builder()
                                .domainName(apiDomain)
                                .hostedZoneId(dnsZoneId)
                                .build()))
                .build();

        apiCertificate.addMetadata("SamResourceId", "ApiCertificate");

        CfnRole asyncFunctionRole = CfnRole.Builder.create(this, "AsyncFunctionRole")
                .roleName("async-function-role")
                .assumeRolePolicyDocument(Map.of("Version", "2012-10-17",
                "Statement", Arrays.asList(
                        Map.of("Effect", "Allow",
                        "Principal", Map.of("Service", Arrays.asList(
                                "lambda.amazonaws.com")),
                        "Action", Arrays.asList(
                                "sts:AssumeRole")))))
                .managedPolicyArns(Arrays.asList(
                        "arn:aws:iam::aws:policy/AWSXrayWriteOnlyAccess",
                        "arn:aws:iam::aws:policy/service-role/AWSLambdaBasicExecutionRole",
                        "arn:aws:iam::aws:policy/AmazonSQSFullAccess",
                        "arn:aws:iam::aws:policy/AmazonS3FullAccess"))
                .build();

        asyncFunctionRole.addMetadata("SamResourceId", "AsyncFunctionRole");

        CfnQueue errorQueue = CfnQueue.Builder.create(this, "ErrorQueue")
                .queueName("jarhc-online-error-queue")
                .messageRetentionPeriod(1209600)
                .build();

        errorQueue.addMetadata("SamResourceId", "ErrorQueue");

        CfnRole restApiFunctionRole = CfnRole.Builder.create(this, "RestApiFunctionRole")
                .roleName("rest-api-role")
                .assumeRolePolicyDocument(Map.of("Version", "2012-10-17",
                "Statement", Arrays.asList(
                        Map.of("Effect", "Allow",
                        "Principal", Map.of("Service", Arrays.asList(
                                "lambda.amazonaws.com")),
                        "Action", Arrays.asList(
                                "sts:AssumeRole")))))
                .managedPolicyArns(Arrays.asList(
                        "arn:aws:iam::aws:policy/AWSXrayWriteOnlyAccess",
                        "arn:aws:iam::aws:policy/service-role/AWSLambdaBasicExecutionRole",
                        "arn:aws:iam::aws:policy/service-role/AWSLambdaRole",
                        "arn:aws:iam::aws:policy/AmazonS3ReadOnlyAccess"))
                .build();

        restApiFunctionRole.addMetadata("SamResourceId", "RestApiFunctionRole");

        CfnDomainName apiGatewayDomainNamed39b733ace = CfnDomainName.Builder.create(this, "ApiGatewayDomainNamed39b733ace")
                .regionalCertificateArn(apiCertificate.getRef())
                .domainName("api.jarhc.org")
                .endpointConfiguration(CfnDomainName.EndpointConfigurationProperty.builder()
                        .types(Arrays.asList(
                                "REGIONAL"))
                        .build())
                .build();

        CfnFunction japiccCheckFunction = CfnFunction.Builder.create(this, "JapiccCheckFunction")
                .code(CfnFunction.CodeProperty.builder()
                        .imageUri("837783538267.dkr.ecr.eu-central-1.amazonaws.com/jarhc-online-api:japicccheckfunction-adb1ce521f2b-v1")
                        .build())
                .packageType("Image")
                .deadLetterConfig(CfnFunction.DeadLetterConfigProperty.builder()
                        .targetArn(errorQueue.getAttrArn())
                        .build())
                .functionName("japicc-check")
                .memorySize(1024)
                .role(asyncFunctionRole.getAttrArn())
                .timeout(60)
                .environment(CfnFunction.EnvironmentProperty.builder()
                        .variables(Map.of("BUCKET_NAME", websiteBucketName,
                        "BUCKET_URL", websiteBucketUrl))
                        .build())
                .tags(Arrays.asList(
                        CfnTag.builder()
                                .key("lambda:createdBy")
                                .value("SAM")
                                .build()))
                .tracingConfig(CfnFunction.TracingConfigProperty.builder()
                        .mode("Active")
                        .build())
                .architectures(Arrays.asList(
                        "x86_64"))
                .build();

        japiccCheckFunction.addMetadata("DockerContext", "/Users/markwalder/Documents/Private/IntelliJ/jarhc-online/sam-api/japicc-check");
        japiccCheckFunction.addMetadata("DockerTag", "v1");
        japiccCheckFunction.addMetadata("Dockerfile", "Dockerfile");
        japiccCheckFunction.addMetadata("SamResourceId", "JapiccCheckFunction");

        CfnFunction jarhcCheckFunction = CfnFunction.Builder.create(this, "JarhcCheckFunction")
                .code(CfnFunction.CodeProperty.builder()
                        .s3Bucket("aws-sam-cli-managed-default-samclisourcebucket-lcbyi0cw90q3")
                        .s3Key("jarhc-online-api/0e7882a16924b5dce32769213f64627d")
                        .build())
                .deadLetterConfig(CfnFunction.DeadLetterConfigProperty.builder()
                        .targetArn(errorQueue.getAttrArn())
                        .build())
                .functionName("jarhc-check")
                .handler("org.jarhc.online.jarhc.Handler")
                .memorySize(1024)
                .role(asyncFunctionRole.getAttrArn())
                .runtime("java11")
                .timeout(60)
                .environment(CfnFunction.EnvironmentProperty.builder()
                        .variables(Map.of("BUCKET_NAME", websiteBucketName,
                        "BUCKET_URL", websiteBucketUrl))
                        .build())
                .tags(Arrays.asList(
                        CfnTag.builder()
                                .key("lambda:createdBy")
                                .value("SAM")
                                .build()))
                .tracingConfig(CfnFunction.TracingConfigProperty.builder()
                        .mode("Active")
                        .build())
                .architectures(Arrays.asList(
                        "x86_64"))
                .build();

        jarhcCheckFunction.addMetadata("SamResourceId", "JarhcCheckFunction");

        CfnFunction restApiFunction = CfnFunction.Builder.create(this, "RestApiFunction")
                .code(CfnFunction.CodeProperty.builder()
                        .s3Bucket("aws-sam-cli-managed-default-samclisourcebucket-lcbyi0cw90q3")
                        .s3Key("jarhc-online-api/2d1a8b550a8d0086639fe92705c1363d")
                        .build())
                .functionName("rest-api")
                .handler("org.jarhc.online.rest.Handler")
                .memorySize(512)
                .role(restApiFunctionRole.getAttrArn())
                .runtime("java11")
                .timeout(10)
                .environment(CfnFunction.EnvironmentProperty.builder()
                        .variables(Map.of("BUCKET_NAME", websiteBucketName,
                        "BUCKET_URL", websiteBucketUrl,
                        "JAVA_TOOL_OPTIONS", "-XX:+TieredCompilation -XX:TieredStopAtLevel=1"))
                        .build())
                .tags(Arrays.asList(
                        CfnTag.builder()
                                .key("lambda:createdBy")
                                .value("SAM")
                                .build()))
                .tracingConfig(CfnFunction.TracingConfigProperty.builder()
                        .mode("Active")
                        .build())
                .architectures(Arrays.asList(
                        "x86_64"))
                .build();

        restApiFunction.addMetadata("SamResourceId", "RestApiFunction");

        CfnVersion japiccCheckFunctionVersion1d0277932a = CfnVersion.Builder.create(this, "JapiccCheckFunctionVersion1d0277932a")
                .functionName(japiccCheckFunction.getRef())
                .build();

        japiccCheckFunctionVersion1d0277932a.applyRemovalPolicy(RemovalPolicy.RETAIN);

        CfnVersion jarhcCheckFunctionVersionc64849b36b = CfnVersion.Builder.create(this, "JarhcCheckFunctionVersionc64849b36b")
                .functionName(jarhcCheckFunction.getRef())
                .build();

        jarhcCheckFunctionVersionc64849b36b.applyRemovalPolicy(RemovalPolicy.RETAIN);

        CfnRecordSetGroup recordSetGroup877b93814a = CfnRecordSetGroup.Builder.create(this, "RecordSetGroup877b93814a")
                .hostedZoneId("Z39VOOPW73P7H0")
                .recordSets(Arrays.asList(
                        CfnRecordSetGroup.RecordSetProperty.builder()
                                .name("api.jarhc.org")
                                .type("A")
                                .aliasTarget(CfnRecordSetGroup.AliasTargetProperty.builder()
                                        .evaluateTargetHealth(false)
                                        .hostedZoneId(apiGatewayDomainNamed39b733ace.getAttrRegionalHostedZoneId())
                                        .dnsName(apiGatewayDomainNamed39b733ace.getAttrRegionalDomainName())
                                        .build())
                                .build()))
                .build();

        CfnVersion restApiFunctionVersionfd84d95e06 = CfnVersion.Builder.create(this, "RestApiFunctionVersionfd84d95e06")
                .functionName(restApiFunction.getRef())
                .build();

        restApiFunctionVersionfd84d95e06.applyRemovalPolicy(RemovalPolicy.RETAIN);

        CfnAlias japiccCheckFunctionAliasLive = CfnAlias.Builder.create(this, "JapiccCheckFunctionAliasLive")
                .name("Live")
                .functionName(japiccCheckFunction.getRef())
                .functionVersion(japiccCheckFunctionVersion1d0277932a.getAttrVersion())
                .build();

        CfnAlias jarhcCheckFunctionAliasLive = CfnAlias.Builder.create(this, "JarhcCheckFunctionAliasLive")
                .name("Live")
                .functionName(jarhcCheckFunction.getRef())
                .functionVersion(jarhcCheckFunctionVersionc64849b36b.getAttrVersion())
                .build();

        CfnAlias restApiFunctionAliasLive = CfnAlias.Builder.create(this, "RestApiFunctionAliasLive")
                .name("Live")
                .functionName(restApiFunction.getRef())
                .functionVersion(restApiFunctionVersionfd84d95e06.getAttrVersion())
                .build();

        CfnEventInvokeConfig japiccCheckFunctionEventInvokeConfig = CfnEventInvokeConfig.Builder.create(this, "JapiccCheckFunctionEventInvokeConfig")
                .destinationConfig(CfnEventInvokeConfig.DestinationConfigProperty.builder()
                        .build())
                .functionName(japiccCheckFunction.getRef())
                .maximumEventAgeInSeconds(120)
                .maximumRetryAttempts(0)
                .qualifier("Live")
                .build();

        japiccCheckFunctionEventInvokeConfig.addMetadata("DockerContext", "/Users/markwalder/Documents/Private/IntelliJ/jarhc-online/sam-api/japicc-check");
        japiccCheckFunctionEventInvokeConfig.addMetadata("DockerTag", "v1");
        japiccCheckFunctionEventInvokeConfig.addMetadata("Dockerfile", "Dockerfile");
        japiccCheckFunctionEventInvokeConfig.addMetadata("SamResourceId", "JapiccCheckFunction");
        japiccCheckFunctionEventInvokeConfig.addDependency(japiccCheckFunctionAliasLive);

        CfnEventInvokeConfig jarhcCheckFunctionEventInvokeConfig = CfnEventInvokeConfig.Builder.create(this, "JarhcCheckFunctionEventInvokeConfig")
                .destinationConfig(CfnEventInvokeConfig.DestinationConfigProperty.builder()
                        .build())
                .functionName(jarhcCheckFunction.getRef())
                .maximumEventAgeInSeconds(120)
                .maximumRetryAttempts(0)
                .qualifier("Live")
                .build();

        jarhcCheckFunctionEventInvokeConfig.addMetadata("SamResourceId", "JarhcCheckFunction");
        jarhcCheckFunctionEventInvokeConfig.addDependency(jarhcCheckFunctionAliasLive);

        CfnRestApi restApi = CfnRestApi.Builder.create(this, "RestApi")
                .body(Map.of("info", Map.of("version", "1.0",
                "title", this.getStackName()),
                "paths", Map.of("/japicc/submit", Map.of("post", Map.of("x-amazon-apigateway-integration", Map.of("httpMethod", "POST",
                "type", "aws_proxy",
                "uri", "arn:aws:apigateway:" + this.getRegion() + ":lambda:path/2015-03-31/functions/" + restApiFunctionAliasLive.getRef() + "/invocations"),
                "x-amazon-apigateway-request-validator", "body-only",
                "security", Arrays.asList(
                        Map.of("CognitoAuthorizer", Arrays.asList(
                        )),
                "parameters", Arrays.asList(
                        Map.of("required", true,
                        "in", "body",
                        "name", "japiccsubmitmodel",
                        "schema", Map.of("$ref", "#/definitions/japiccsubmitmodel"))),
                "responses", Map.of(),
                "options", Map.of("x-amazon-apigateway-integration", Map.of("type", "mock",
                "requestTemplates", Map.of("application/json", """
                {
                  "statusCode" : 200
                }
                """),
                "responses", Map.of("default", Map.of("statusCode", "200",
                "responseTemplates", Map.of("application/json", "{}
                "),
                "responseParameters", Map.of("method.response.header.Access-Control-Allow-Headers", "'Content-Type,Authorization'",
                "method.response.header.Access-Control-Allow-Origin", "'" + websiteUrl + "'",
                "method.response.header.Access-Control-Max-Age", "'600'",
                "method.response.header.Access-Control-Allow-Methods", "'GET,POST,OPTIONS'",
                "method.response.header.Access-Control-Allow-Credentials", "'true'")))),
                "consumes", Arrays.asList(
                        "application/json"),
                "summary", "CORS support",
                "responses", Map.of("200", Map.of("headers", Map.of("Access-Control-Allow-Origin", Map.of("type", "string"),
                "Access-Control-Allow-Headers", Map.of("type", "string"),
                "Access-Control-Max-Age", Map.of("type", "integer"),
                "Access-Control-Allow-Methods", Map.of("type", "string"),
                "Access-Control-Allow-Credentials", Map.of("type", "string")),
                "description", "Default response for CORS method")),
                "produces", Arrays.asList(
                        "application/json"))),
                "/auth/validate", Map.of("options", Map.of("x-amazon-apigateway-integration", Map.of("type", "mock",
                "requestTemplates", Map.of("application/json", """
                {
                  "statusCode" : 200
                }
                """),
                "responses", Map.of("default", Map.of("statusCode", "200",
                "responseTemplates", Map.of("application/json", "{}
                "),
                "responseParameters", Map.of("method.response.header.Access-Control-Allow-Headers", "'Content-Type,Authorization'",
                "method.response.header.Access-Control-Allow-Origin", "'" + websiteUrl + "'",
                "method.response.header.Access-Control-Max-Age", "'600'",
                "method.response.header.Access-Control-Allow-Methods", "'GET,POST,OPTIONS'",
                "method.response.header.Access-Control-Allow-Credentials", "'true'")))),
                "consumes", Arrays.asList(
                        "application/json"),
                "summary", "CORS support",
                "responses", Map.of("200", Map.of("headers", Map.of("Access-Control-Allow-Origin", Map.of("type", "string"),
                "Access-Control-Allow-Headers", Map.of("type", "string"),
                "Access-Control-Max-Age", Map.of("type", "integer"),
                "Access-Control-Allow-Methods", Map.of("type", "string"),
                "Access-Control-Allow-Credentials", Map.of("type", "string")),
                "description", "Default response for CORS method")),
                "produces", Arrays.asList(
                        "application/json")),
                "get", Map.of("x-amazon-apigateway-integration", Map.of("httpMethod", "POST",
                "type", "aws_proxy",
                "uri", "arn:aws:apigateway:" + this.getRegion() + ":lambda:path/2015-03-31/functions/" + restApiFunctionAliasLive.getRef() + "/invocations"),
                "security", Arrays.asList(
                        Map.of("CognitoAuthorizer", Arrays.asList(
                        )),
                "responses", Map.of()),
                "/jarhc/submit", Map.of("post", Map.of("x-amazon-apigateway-integration", Map.of("httpMethod", "POST",
                "type", "aws_proxy",
                "uri", "arn:aws:apigateway:" + this.getRegion() + ":lambda:path/2015-03-31/functions/" + restApiFunctionAliasLive.getRef() + "/invocations"),
                "x-amazon-apigateway-request-validator", "body-only",
                "security", Arrays.asList(
                        Map.of("CognitoAuthorizer", Arrays.asList(
                        )),
                "parameters", Arrays.asList(
                        Map.of("required", true,
                        "in", "body",
                        "name", "jarhcsubmitmodel",
                        "schema", Map.of("$ref", "#/definitions/jarhcsubmitmodel"))),
                "responses", Map.of(),
                "options", Map.of("x-amazon-apigateway-integration", Map.of("type", "mock",
                "requestTemplates", Map.of("application/json", """
                {
                  "statusCode" : 200
                }
                """),
                "responses", Map.of("default", Map.of("statusCode", "200",
                "responseTemplates", Map.of("application/json", "{}
                "),
                "responseParameters", Map.of("method.response.header.Access-Control-Allow-Headers", "'Content-Type,Authorization'",
                "method.response.header.Access-Control-Allow-Origin", "'" + websiteUrl + "'",
                "method.response.header.Access-Control-Max-Age", "'600'",
                "method.response.header.Access-Control-Allow-Methods", "'GET,POST,OPTIONS'",
                "method.response.header.Access-Control-Allow-Credentials", "'true'")))),
                "consumes", Arrays.asList(
                        "application/json"),
                "summary", "CORS support",
                "responses", Map.of("200", Map.of("headers", Map.of("Access-Control-Allow-Origin", Map.of("type", "string"),
                "Access-Control-Allow-Headers", Map.of("type", "string"),
                "Access-Control-Max-Age", Map.of("type", "integer"),
                "Access-Control-Allow-Methods", Map.of("type", "string"),
                "Access-Control-Allow-Credentials", Map.of("type", "string")),
                "description", "Default response for CORS method")),
                "produces", Arrays.asList(
                        "application/json"))),
                "/maven/search", Map.of("post", Map.of("x-amazon-apigateway-integration", Map.of("httpMethod", "POST",
                "type", "aws_proxy",
                "uri", "arn:aws:apigateway:" + this.getRegion() + ":lambda:path/2015-03-31/functions/" + restApiFunctionAliasLive.getRef() + "/invocations"),
                "x-amazon-apigateway-request-validator", "body-only",
                "security", Arrays.asList(
                        Map.of("NONE", Arrays.asList(
                        )),
                "parameters", Arrays.asList(
                        Map.of("required", true,
                        "in", "body",
                        "name", "mavensearchmodel",
                        "schema", Map.of("$ref", "#/definitions/mavensearchmodel"))),
                "responses", Map.of(),
                "options", Map.of("x-amazon-apigateway-integration", Map.of("type", "mock",
                "requestTemplates", Map.of("application/json", """
                {
                  "statusCode" : 200
                }
                """),
                "responses", Map.of("default", Map.of("statusCode", "200",
                "responseTemplates", Map.of("application/json", "{}
                "),
                "responseParameters", Map.of("method.response.header.Access-Control-Allow-Headers", "'Content-Type,Authorization'",
                "method.response.header.Access-Control-Allow-Origin", "'" + websiteUrl + "'",
                "method.response.header.Access-Control-Max-Age", "'600'",
                "method.response.header.Access-Control-Allow-Methods", "'GET,POST,OPTIONS'",
                "method.response.header.Access-Control-Allow-Credentials", "'true'")))),
                "consumes", Arrays.asList(
                        "application/json"),
                "summary", "CORS support",
                "responses", Map.of("200", Map.of("headers", Map.of("Access-Control-Allow-Origin", Map.of("type", "string"),
                "Access-Control-Allow-Headers", Map.of("type", "string"),
                "Access-Control-Max-Age", Map.of("type", "integer"),
                "Access-Control-Allow-Methods", Map.of("type", "string"),
                "Access-Control-Allow-Credentials", Map.of("type", "string")),
                "description", "Default response for CORS method")),
                "produces", Arrays.asList(
                        "application/json")))),
                "x-amazon-apigateway-gateway-responses", Map.of("DEFAULT_5XX", Map.of("responseParameters", Map.of("gatewayresponse.header.Access-Control-Allow-Origin", "'" + websiteUrl + "'",
                "gatewayresponse.header.Access-Control-Allow-Credentials", "'true'"),
                "responseTemplates", Map.of(),
                "DEFAULT_4XX", Map.of("responseParameters", Map.of("gatewayresponse.header.Access-Control-Allow-Origin", "'" + websiteUrl + "'",
                "gatewayresponse.header.Access-Control-Allow-Credentials", "'true'"),
                "responseTemplates", Map.of()),
                "securityDefinitions", Map.of("CognitoAuthorizer", Map.of("in", "header",
                "type", "apiKey",
                "name", "Authorization",
                "x-amazon-apigateway-authorizer", Map.of("providerARNs", Arrays.asList(
                        "arn:aws:cognito-idp:eu-central-1:837783538267:userpool/eu-central-1_duUV8DgO7"),
                "type", "cognito_user_pools"),
                "x-amazon-apigateway-authtype", "cognito_user_pools")),
                "definitions", Map.of("jarhcsubmitmodel", Map.of("additionalProperties", false,
                "required", Arrays.asList(
                        "classpath"),
                "type", "object",
                "properties", Map.of("provided", Map.of("items", Map.of("type", "string"),
                "type", "array"),
                "classpath", Map.of("items", Map.of("type", "string"),
                "type", "array"))),
                "mavensearchmodel", Map.of("additionalProperties", false,
                "required", Arrays.asList(
                        "coordinates"),
                "type", "object",
                "properties", Map.of("coordinates", Map.of("type", "string"))),
                "japiccsubmitmodel", Map.of("additionalProperties", false,
                "required", Arrays.asList(
                        "oldVersion",
                        "newVersion"),
                "type", "object",
                "properties", Map.of("newVersion", Map.of("type", "string"),
                "oldVersion", Map.of("type", "string")))),
                "swagger", "2.0",
                "x-amazon-apigateway-request-validators", Map.of("body-only", Map.of("validateRequestParameters", false,
                "validateRequestBody", true))))
                .parameters(Map.of("endpointConfigurationTypes", "REGIONAL"))
                .endpointConfiguration(CfnRestApi.EndpointConfigurationProperty.builder()
                        .types(Arrays.asList(
                                "REGIONAL"))
                        .build())
                .build();

        restApi.addMetadata("SamResourceId", "RestApi");

        CfnDeployment restApiDeploymentda0c697d37 = CfnDeployment.Builder.create(this, "RestApiDeploymentda0c697d37")
                .description("RestApi deployment id: da0c697d373d21ac37e4fed735198b7705ad0e81")
                .restApiId(restApi.getRef())
                .stageName("Stage")
                .build();

        CfnPermission restApiFunctionAuthValidatePermissionProd = CfnPermission.Builder.create(this, "RestApiFunctionAuthValidatePermissionProd")
                .action("lambda:InvokeFunction")
                .functionName(restApiFunctionAliasLive.getRef())
                .principal("apigateway.amazonaws.com")
                .sourceArn("arn:aws:execute-api:" + this.getRegion() + ":" + this.getAccount() + ":" + restApi.getRef() + "/" + "*" + "/GET/auth/validate")
                .build();

        CfnPermission restApiFunctionJapiccSubmitPermissionProd = CfnPermission.Builder.create(this, "RestApiFunctionJapiccSubmitPermissionProd")
                .action("lambda:InvokeFunction")
                .functionName(restApiFunctionAliasLive.getRef())
                .principal("apigateway.amazonaws.com")
                .sourceArn("arn:aws:execute-api:" + this.getRegion() + ":" + this.getAccount() + ":" + restApi.getRef() + "/" + "*" + "/POST/japicc/submit")
                .build();

        CfnPermission restApiFunctionJarhcSubmitPermissionProd = CfnPermission.Builder.create(this, "RestApiFunctionJarhcSubmitPermissionProd")
                .action("lambda:InvokeFunction")
                .functionName(restApiFunctionAliasLive.getRef())
                .principal("apigateway.amazonaws.com")
                .sourceArn("arn:aws:execute-api:" + this.getRegion() + ":" + this.getAccount() + ":" + restApi.getRef() + "/" + "*" + "/POST/jarhc/submit")
                .build();

        CfnPermission restApiFunctionMavenSearchPermissionProd = CfnPermission.Builder.create(this, "RestApiFunctionMavenSearchPermissionProd")
                .action("lambda:InvokeFunction")
                .functionName(restApiFunctionAliasLive.getRef())
                .principal("apigateway.amazonaws.com")
                .sourceArn("arn:aws:execute-api:" + this.getRegion() + ":" + this.getAccount() + ":" + restApi.getRef() + "/" + "*" + "/POST/maven/search")
                .build();

        CfnStage restApiProdStage = CfnStage.Builder.create(this, "RestApiProdStage")
                .deploymentId(restApiDeploymentda0c697d37.getRef())
                .restApiId(restApi.getRef())
                .stageName("Prod")
                .methodSettings(Arrays.asList(
                        CfnStage.MethodSettingProperty.builder()
                                .httpMethod("*")
                                .resourcePath("/*")
                                .throttlingBurstLimit(20)
                                .throttlingRateLimit(10)
                                .build()))
                .build();

        CfnBasePathMapping restApiBasePathMapping = CfnBasePathMapping.Builder.create(this, "RestApiBasePathMapping")
                .domainName(apiGatewayDomainNamed39b733ace.getRef())
                .restApiId(restApi.getRef())
                .stage(restApiProdStage.getRef())
                .build();

        this.apiGatewayUrl = "https://" + restApi.getRef() + ".execute-api." + this.getRegion() + ".amazonaws.com/Prod/";
        CfnOutput.Builder.create(this, "CfnOutputApiGatewayUrl")
                .key("ApiGatewayUrl")
                .value(this.apiGatewayUrl.toString())
                .description("Base URL of API Gateway.")
                .build();

    }
}
