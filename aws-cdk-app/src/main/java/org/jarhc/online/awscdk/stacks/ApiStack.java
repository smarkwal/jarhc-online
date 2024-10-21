package org.jarhc.online.awscdk.stacks;

import java.util.Map;
import software.amazon.awscdk.RemovalPolicy;
import software.amazon.awscdk.Stack;
import software.amazon.awscdk.StackProps;
import software.amazon.awscdk.services.apigateway.CfnBasePathMapping;
import software.amazon.awscdk.services.apigateway.CfnDeployment;
import software.amazon.awscdk.services.apigateway.CfnDomainName;
import software.amazon.awscdk.services.apigateway.CfnRestApi;
import software.amazon.awscdk.services.apigateway.CfnStage;
import software.amazon.awscdk.services.certificatemanager.CfnCertificate;
import software.amazon.awscdk.services.iam.CfnRole;
import software.amazon.awscdk.services.lambda.CfnAlias;
import software.amazon.awscdk.services.lambda.CfnEventInvokeConfig;
import software.amazon.awscdk.services.lambda.CfnFunction;
import software.amazon.awscdk.services.lambda.CfnPermission;
import software.amazon.awscdk.services.lambda.CfnVersion;
import software.amazon.awscdk.services.route53.CfnRecordSetGroup;
import software.amazon.awscdk.services.sqs.CfnQueue;
import software.constructs.Construct;

public class ApiStack extends AbstractStack {

	public ApiStack(
			final Construct scope,
			final String id,
			final StackProps props,
			// configuration:
			final String apiDomain,
			final String cognitoUserPoolArn,
			final String websiteUrl,
			final String websiteBucketName,
			final String websiteBucketUrl,
			final String dnsZoneId
	) {
		super(scope, id, props);

		// ================================================================================================================
		// Lambda functions

		// IAM role for asynchronous Lambda functions
		CfnRole asyncFunctionRole = CfnRole.Builder.create(this, "AsyncFunctionRole")
				.roleName("async-function-role")
				.assumeRolePolicyDocument(
						map(
								"Version", "2012-10-17",
								"Statement", list(
										map(
												"Effect", "Allow",
												"Principal", map(
														"Service", list("lambda.amazonaws.com")
												),
												"Action", list("sts:AssumeRole")
										)
								)
						)
				)
				.managedPolicyArns(
						list(
								"arn:aws:iam::aws:policy/AWSXrayWriteOnlyAccess",
								"arn:aws:iam::aws:policy/service-role/AWSLambdaBasicExecutionRole",
								"arn:aws:iam::aws:policy/AmazonSQSFullAccess",
								"arn:aws:iam::aws:policy/AmazonS3FullAccess"
						)
				)
				.build();

		// IAM role for REST API Lambda function
		CfnRole restApiFunctionRole = CfnRole.Builder.create(this, "RestApiFunctionRole")
				.roleName("rest-api-role")
				.assumeRolePolicyDocument(
						map(
								"Version", "2012-10-17",
								"Statement", list(
										map(
												"Effect", "Allow",
												"Principal", map(
														"Service", list("lambda.amazonaws.com")
												),
												"Action", list("sts:AssumeRole")
										)
								)
						)
				)
				.managedPolicyArns(
						list(
								"arn:aws:iam::aws:policy/AWSXrayWriteOnlyAccess",
								"arn:aws:iam::aws:policy/service-role/AWSLambdaBasicExecutionRole",
								"arn:aws:iam::aws:policy/service-role/AWSLambdaRole",
								"arn:aws:iam::aws:policy/AmazonS3ReadOnlyAccess")
				)
				.build();

		// Dead letter queue for async Lambda functions
		CfnQueue errorQueue = CfnQueue.Builder.create(this, "ErrorQueue")
				.queueName("jarhc-online-error-queue")
				.messageRetentionPeriod(1209600) // 14 days
				.build();

		CfnFunction.DeadLetterConfigProperty deadLetterConfig = CfnFunction.DeadLetterConfigProperty.builder()
				.targetArn(errorQueue.getAttrArn())
				.build();

		CfnFunction.TracingConfigProperty tracingConfig = CfnFunction.TracingConfigProperty.builder()
				.mode("Active")
				.build();

		CfnFunction.EnvironmentProperty environment = CfnFunction.EnvironmentProperty.builder()
				.variables(map(
						"BUCKET_NAME", websiteBucketName,
						"BUCKET_URL", websiteBucketUrl,
						// TODO: disable JAVA_TOOL_OPTIONS for JAPICC and JarHC functions?
						"JAVA_TOOL_OPTIONS", "-XX:+TieredCompilation -XX:TieredStopAtLevel=1"
				))
				.build();

		// TODO: create ECR repository for Docker images?
		// TODO: create S3 bucket for JAR/ZIP files?

		// Lambda function for JAPICC Check (invoked asynchronously)
		// TODO: fix code reference:
		//   - build Docker image
		//   - push image to ECR
		//   - set image URI
		CfnFunction.CodeProperty japiccCheckCode = CfnFunction.CodeProperty.builder()
				.imageUri(this.getAccount() + ".dkr.ecr.eu-central-1.amazonaws.com/jarhc-online-code:japicc-check-function-v1")
				.build();

		CfnFunction japiccCheckFunction = CfnFunction.Builder.create(this, "JapiccCheckFunction")
				.functionName("japicc-check")
				.packageType("Image")
				.architectures(list("x86_64"))
				.code(japiccCheckCode)
				.tracingConfig(tracingConfig)
				.role(asyncFunctionRole.getAttrArn())
				// max price per execution:
				// memorySize * timeout / 1024 * $0.0000166667 = $0.001000002
				.memorySize(1024) // increase memory size to 1 GB for JAPICC
				.timeout(60) // increase timeout to 60 seconds for JAPICC
				.deadLetterConfig(deadLetterConfig)
				.environment(environment)
				.build();

		CfnAlias japiccCheckFunctionAlias = createFunctionVersion(this, japiccCheckFunction);
		createEvenInvokeConfig(this, japiccCheckFunction, japiccCheckFunctionAlias);

		// Lambda function for JarHC Check (invoked asynchronously)
		// TODO: fix code reference:
		//   - build function uber-JAR file with all dependencies
		//   - upload JAR file to S3
		//   - set S3 bucket and key
		CfnFunction.CodeProperty jarhcCheckCode = CfnFunction.CodeProperty.builder()
				.s3Bucket("jarhc-online-code")
				.s3Key("jarhc-check-1.0.0.jar")
				.build();

		CfnFunction jarhcCheckFunction = CfnFunction.Builder.create(this, "JarhcCheckFunction")
				.functionName("jarhc-check")
				.code(jarhcCheckCode)
				.handler("org.jarhc.online.jarhc.Handler")
				.runtime("java11") // TODO: migrate to Java 17
				.architectures(list("x86_64")) // TODO: migrate to ARM64?
				.tracingConfig(tracingConfig)
				.role(asyncFunctionRole.getAttrArn())
				// max price per execution:
				// memorySize * timeout / 1024 * $0.0000166667 = $0.001000002
				.memorySize(1024) // increase memory size to 1 GB for JarHC
				.timeout(60) // increase timeout to 60 seconds for JarHC
				.deadLetterConfig(deadLetterConfig)
				.environment(environment)
				.build();

		CfnAlias jarhcCheckFunctionAlias = createFunctionVersion(this, jarhcCheckFunction);
		createEvenInvokeConfig(this, jarhcCheckFunction, jarhcCheckFunctionAlias);

		// Lambda function for REST API (invoked synchronously by API Gateway)
		// TODO: fix code reference:
		//   - build function uber-JAR file with all dependencies
		//   - upload JAR file to S3
		//   - set S3 bucket and key
		CfnFunction.CodeProperty restApiCode = CfnFunction.CodeProperty.builder()
				.s3Bucket("jarhc-online-code")
				.s3Key("rest-api-1.0.0.jar") // TODO: use fat JAR with all dependencies
				.build();

		CfnFunction restApiFunction = CfnFunction.Builder.create(this, "RestApiFunction")
				.functionName("rest-api")
				.code(restApiCode)
				.handler("org.jarhc.online.rest.Handler")
				.runtime("java11") // TODO: migrate to Java 17
				.architectures(list("x86_64")) // TODO: migrate to ARM64?
				// TODO: SnapStart:
				//   ApplyOn: PublishedVersions
				.tracingConfig(tracingConfig)
				.role(restApiFunctionRole.getAttrArn())
				// max price per execution:
				// memorySize * timeout / 1024 * $0.0000166667 = $0.0000833335
				.memorySize(512) // increase memory size to 512 MB for REST API
				.timeout(10) // increase timeout to 10 seconds for REST API
				.environment(environment)
				.build();

		CfnAlias restApiFunctionAlias = createFunctionVersion(this, restApiFunction);

		// ================================================================================================================
		// API Gateway

		// SSL certificate for API Gateway
		CfnCertificate apiCertificate = CfnCertificate.Builder.create(this, "ApiCertificate")
				.domainName(apiDomain)
				.validationMethod("DNS")
				.domainValidationOptions(
						list(
								CfnCertificate.DomainValidationOptionProperty.builder()
										.domainName(apiDomain)
										.hostedZoneId(dnsZoneId)
										.build()
						)
				)
				.build();

		// API Gateway domain name
		CfnDomainName apiGatewayDomainName = CfnDomainName.Builder.create(this, "ApiGatewayDomainName")
				.regionalCertificateArn(apiCertificate.getRef())
				.domainName(apiDomain)
				.endpointConfiguration(
						CfnDomainName.EndpointConfigurationProperty.builder()
								.types(list("REGIONAL"))
								.build()
				)
				.build();

		// Route 53 DNS record for API Gateway domain
		CfnRecordSetGroup.Builder.create(this, "RecordSetGroup")
				.hostedZoneId(dnsZoneId)
				.recordSets(
						list(
								CfnRecordSetGroup.RecordSetProperty.builder()
										.name(apiDomain)
										.type("A")
										.aliasTarget(
												CfnRecordSetGroup.AliasTargetProperty.builder()
														.evaluateTargetHealth(false)
														.hostedZoneId(apiGatewayDomainName.getAttrRegionalHostedZoneId())
														.dnsName(apiGatewayDomainName.getAttrRegionalDomainName())
														.build()
										)
										.build()
						)
				)
				.build();

		var gatewayErrorResponse = map(
				"responseParameters", map(
						"gatewayresponse.header.Access-Control-Allow-Origin", "'" + websiteUrl + "'",
						"gatewayresponse.header.Access-Control-Allow-Credentials", "'true'"
				),
				"responseTemplates", map()
		);

		// REST API Gateway
		CfnRestApi restApi = CfnRestApi.Builder.create(this, "RestApi")
				.body(
						map(
								"info", map(
										"version", "1.0",
										"title", this.getStackName()
								),
								"paths", createRestApiPaths(websiteUrl, restApiFunctionAlias),
								"x-amazon-apigateway-gateway-responses", map(
										"DEFAULT_5XX", gatewayErrorResponse,
										"DEFAULT_4XX", gatewayErrorResponse
								),
								"securityDefinitions", map(
										"CognitoAuthorizer", map(
												"in", "header",
												"type", "apiKey",
												"name", "Authorization",
												"x-amazon-apigateway-authorizer", map(
														"providerARNs", list(cognitoUserPoolArn),
														"type", "cognito_user_pools"
												),
												"x-amazon-apigateway-authtype", "cognito_user_pools"
										)
								),
								"definitions", createRestApiModels(),
								"swagger", "2.0",
								"x-amazon-apigateway-request-validators", map(
										"body-only", map(
												"validateRequestParameters", false,
												"validateRequestBody", true
										)
								)
						)
				)
				.parameters(map(
						"endpointConfigurationTypes", "REGIONAL" // default
				))
				.endpointConfiguration(
						CfnRestApi.EndpointConfigurationProperty.builder()
								.types(list("REGIONAL"))
								.build()
				)
				.build();

		CfnDeployment restApiDeployment = CfnDeployment.Builder.create(this, "RestApiDeployment")
				.restApiId(restApi.getRef())
				.stageName("Stage")
				.build();

		createPermission(this, "AuthValidate", restApiFunctionAlias, restApi, "/*/GET/auth/validate");
		createPermission(this, "JapiccSubmit", restApiFunctionAlias, restApi, "/*/POST/japicc/submit");
		createPermission(this, "JarhcSubmit", restApiFunctionAlias, restApi, "/*/POST/jarhc/submit");
		createPermission(this, "MavenSearch", restApiFunctionAlias, restApi, "/*/POST/maven/search");

		CfnStage restApiProdStage = CfnStage.Builder.create(this, "RestApiProdStage")
				.deploymentId(restApiDeployment.getRef())
				.restApiId(restApi.getRef())
				.stageName("Prod")
				.methodSettings(
						list(
								CfnStage.MethodSettingProperty.builder()
										.httpMethod("*")
										.resourcePath("/*")
										.throttlingBurstLimit(20)
										.throttlingRateLimit(10)
										.build()
						)
				)
				.build();

		CfnBasePathMapping.Builder.create(this, "RestApiBasePathMapping")
				.domainName(apiGatewayDomainName.getRef())
				.restApiId(restApi.getRef())
				.stage(restApiProdStage.getRef())
				.build();

		createOutput(
				"ApiGatewayUrl",
				"https://" + restApi.getRef() + ".execute-api." + this.getRegion() + ".amazonaws.com/Prod/",
				"Base URL of API Gateway."
		);

	}

	private Map<String, Object> createRestApiPaths(String websiteUrl, CfnAlias restApiFunctionAlias) {

		var optionsMethod = map(
				"x-amazon-apigateway-integration", map(
						"type", "mock",
						"requestTemplates", map(
								"application/json", "{\"statusCode\" : 200}"
						),
						"responses", map(
								"default", map(
										"statusCode", "200",
										"responseTemplates", map(
												"application/json", "{}"
										),
										"responseParameters", map(
												"method.response.header.Access-Control-Allow-Headers", "'Content-Type,Authorization'",
												"method.response.header.Access-Control-Allow-Origin", "'" + websiteUrl + "'",
												"method.response.header.Access-Control-Max-Age", "'600'", // cache results of a preflight request for 10 minutes
												"method.response.header.Access-Control-Allow-Methods", "'GET,POST,OPTIONS'",
												"method.response.header.Access-Control-Allow-Credentials", "'true'"
										)
								)
						)
				),
				"consumes", list("application/json"),
				"summary", "CORS support",
				"responses", map(
						"200", map(
								"headers", map(
										"Access-Control-Allow-Origin", map("type", "string"),
										"Access-Control-Allow-Headers", map("type", "string"),
										"Access-Control-Max-Age", map("type", "integer"),
										"Access-Control-Allow-Methods", map("type", "string"),
										"Access-Control-Allow-Credentials", map("type", "string")
								),
								"description", "Default response for CORS method"
						)
				),
				"produces", list("application/json")
		);

		var apiGatewayIntegration = map(
				"httpMethod", "POST",
				"type", "aws_proxy",
				"uri", "arn:aws:apigateway:" + this.getRegion() + ":lambda:path/2015-03-31/functions/" + restApiFunctionAlias.getRef() + "/invocations"
		);

		var authValidatePath = map(
				"options", optionsMethod,
				"get", map(
						"x-amazon-apigateway-integration", apiGatewayIntegration,
						"security", list(map("CognitoAuthorizer", list())),
						"responses", map()
				)
		);

		var japiccSubmitPath = map(
				"options", optionsMethod,
				"post", map(
						"x-amazon-apigateway-integration", apiGatewayIntegration,
						"x-amazon-apigateway-request-validator", "body-only",
						"security", list(map("CognitoAuthorizer", list())),
						"parameters", list(
								map(
										"name", "japiccsubmitmodel",
										"required", true,
										"in", "body",
										"schema", map("$ref", "#/definitions/japiccsubmitmodel")
								)
						),
						"responses", map()
				)
		);

		var jarhcSubmitPath = map(
				"options", optionsMethod,
				"post", map(
						"x-amazon-apigateway-integration", apiGatewayIntegration,
						"x-amazon-apigateway-request-validator", "body-only",
						"security", list(map("CognitoAuthorizer", list())),
						"parameters", list(
								map(
										"name", "jarhcsubmitmodel",
										"required", true,
										"in", "body",
										"schema", map("$ref", "#/definitions/jarhcsubmitmodel")
								)
						),
						"responses", map()
				)
		);

		var mavenSearchPath = map(
				"options", optionsMethod,
				"post", map(
						"x-amazon-apigateway-integration", apiGatewayIntegration,
						"x-amazon-apigateway-request-validator", "body-only",
						"security", list(map("NONE", list())),
						"parameters", list(
								map(
										"name", "mavensearchmodel",
										"required", true,
										"in", "body",
										"schema", map("$ref", "#/definitions/mavensearchmodel")
								)
						),
						"responses", map()
				)
		);

		return map(
				"/auth/validate", authValidatePath,
				"/japicc/submit", japiccSubmitPath,
				"/jarhc/submit", jarhcSubmitPath,
				"/maven/search", mavenSearchPath
		);
	}

	private static Map<String, Object> createRestApiModels() {

		var japiccSubmitModel = map(
				"additionalProperties", false,
				"required", list("oldVersion", "newVersion"),
				"type", "object",
				"properties", map(
						"newVersion", map("type", "string"), // pattern: "^[^:]+:[^:]+:[^:]+$"
						"oldVersion", map("type", "string") // pattern: "^[^:]+:[^:]+:[^:]+$"
				)
		);

		var jarhcSubmitModel = map(
				"additionalProperties", false,
				"required", list("classpath"),
				"type", "object",
				"properties", map(
						"provided", map(
								"items", map(
										"type", "string" // pattern: "^[^:]+:[^:]+:[^:]+$"
								),
								"type", "array"
						),
						"classpath", map(
								"items", map(
										"type", "string" // pattern: "^[^:]+:[^:]+:[^:]+$"
								),
								"type", "array"
						)
				)
		);

		var mavenSearchModel = map(
				"additionalProperties", false,
				"required", list("coordinates"),
				"type", "object",
				"properties", map(
						"coordinates", map(
								"type", "string" // pattern: "^[^:]+:[^:]+:[^:]+$"
						)
				)
		);

		return map(
				"japiccsubmitmodel", japiccSubmitModel,
				"jarhcsubmitmodel", jarhcSubmitModel,
				"mavensearchmodel", mavenSearchModel
		);
	}

	private CfnAlias createFunctionVersion(Stack scope, CfnFunction function) {

		CfnVersion functionVersion = CfnVersion.Builder.create(scope, function.getNode().getId() + "Version")
				.functionName(function.getRef())
				.build();
		functionVersion.applyRemovalPolicy(RemovalPolicy.RETAIN);

		return CfnAlias.Builder.create(scope, function.getNode().getId() + "AliasLive")
				.name("Live")
				.functionName(function.getRef())
				.functionVersion(functionVersion.getAttrVersion())
				.build();
	}

	private void createEvenInvokeConfig(Stack scope, CfnFunction function, CfnAlias alias) {

		CfnEventInvokeConfig eventInvokeConfig = CfnEventInvokeConfig.Builder.create(scope, function.getNode().getId() + "EventInvokeConfig")
				.destinationConfig(CfnEventInvokeConfig.DestinationConfigProperty.builder().build())
				.functionName(function.getRef())
				.maximumEventAgeInSeconds(120)
				.maximumRetryAttempts(0)
				.qualifier("Live")
				.build();

		eventInvokeConfig.addDependency(alias);
	}

	private void createPermission(Stack scope, String id, CfnAlias functionAlias, CfnRestApi restApi, String path) {
		CfnPermission.Builder.create(scope, "RestApiFunction" + id + "PermissionProd")
				.action("lambda:InvokeFunction")
				.functionName(functionAlias.getRef())
				.principal("apigateway.amazonaws.com")
				.sourceArn("arn:aws:execute-api:" + scope.getRegion() + ":" + scope.getAccount() + ":" + restApi.getRef() + path)
				.build();
	}

}
