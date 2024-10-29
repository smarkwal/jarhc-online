package org.jarhc.online.awscdk.stacks;

import java.util.List;
import software.amazon.awscdk.Duration;
import software.amazon.awscdk.Size;
import software.amazon.awscdk.StackProps;
import software.amazon.awscdk.services.apigateway.CognitoUserPoolsAuthorizer;
import software.amazon.awscdk.services.apigateway.CorsOptions;
import software.amazon.awscdk.services.apigateway.DomainNameOptions;
import software.amazon.awscdk.services.apigateway.EndpointConfiguration;
import software.amazon.awscdk.services.apigateway.EndpointType;
import software.amazon.awscdk.services.apigateway.GatewayResponseOptions;
import software.amazon.awscdk.services.apigateway.IAuthorizer;
import software.amazon.awscdk.services.apigateway.JsonSchema;
import software.amazon.awscdk.services.apigateway.JsonSchemaType;
import software.amazon.awscdk.services.apigateway.LambdaIntegration;
import software.amazon.awscdk.services.apigateway.LambdaIntegrationOptions;
import software.amazon.awscdk.services.apigateway.MethodOptions;
import software.amazon.awscdk.services.apigateway.Model;
import software.amazon.awscdk.services.apigateway.ModelOptions;
import software.amazon.awscdk.services.apigateway.Resource;
import software.amazon.awscdk.services.apigateway.ResponseType;
import software.amazon.awscdk.services.apigateway.RestApi;
import software.amazon.awscdk.services.apigateway.StageOptions;
import software.amazon.awscdk.services.certificatemanager.Certificate;
import software.amazon.awscdk.services.certificatemanager.CertificateValidation;
import software.amazon.awscdk.services.certificatemanager.ICertificate;
import software.amazon.awscdk.services.cognito.IUserPool;
import software.amazon.awscdk.services.cognito.UserPool;
import software.amazon.awscdk.services.iam.PolicyStatement;
import software.amazon.awscdk.services.lambda.Architecture;
import software.amazon.awscdk.services.lambda.AssetImageCode;
import software.amazon.awscdk.services.lambda.Code;
import software.amazon.awscdk.services.lambda.EventInvokeConfig;
import software.amazon.awscdk.services.lambda.Function;
import software.amazon.awscdk.services.lambda.Handler;
import software.amazon.awscdk.services.lambda.IFunction;
import software.amazon.awscdk.services.lambda.Runtime;
import software.amazon.awscdk.services.lambda.Tracing;
import software.amazon.awscdk.services.route53.ARecord;
import software.amazon.awscdk.services.route53.HostedZone;
import software.amazon.awscdk.services.route53.HostedZoneProviderProps;
import software.amazon.awscdk.services.route53.IHostedZone;
import software.amazon.awscdk.services.route53.RecordTarget;
import software.amazon.awscdk.services.route53.targets.ApiGatewayDomain;
import software.amazon.awscdk.services.s3.Bucket;
import software.amazon.awscdk.services.s3.IBucket;
import software.amazon.awscdk.services.sqs.IQueue;
import software.amazon.awscdk.services.sqs.Queue;
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
			final String rootDomain
	) {
		super(scope, id, props);

		// reference to Route 53 hosted zone for root domain
		IHostedZone hostedZone = HostedZone.fromLookup(this, "HostedZone", HostedZoneProviderProps.builder().domainName(rootDomain).build());

		// SSL certificate for custom domain used by REST API Gateway
		ICertificate apiCertificate = Certificate.Builder.create(this, "ApiCertificate")
				.domainName(apiDomain)
				.validation(CertificateValidation.fromDns(hostedZone))
				.build();

		IBucket websiteBucket = Bucket.fromBucketName(this, "WebsiteBucket", websiteBucketName);

		IUserPool cognitoUserPool = UserPool.fromUserPoolArn(this, "CognitoUserPool", cognitoUserPoolArn);

		IQueue errorQueue = Queue.Builder.create(this, "ErrorQueue")
				.queueName("jarhc-online-error-queue")
				.retentionPeriod(Duration.days(14))
				.build();

		IFunction jarhcCheckFunction = Function.Builder.create(this, "JarhcCheckFunction")
				.functionName("jarhc-check")
				.code(Code.fromAsset("../jarhc-aws-lambda/build/distributions/jarhc-aws-lambda-1.0.0.zip"))
				.handler("org.jarhc.online.jarhc.Handler")
				.runtime(Runtime.JAVA_11) // TODO: migrate to Java 17
				.architecture(Architecture.X86_64) // TODO: migrate to ARM64?
				.tracing(Tracing.ACTIVE)
				// max price per execution:
				// memorySize * timeout / 1024 * $0.0000166667 = $0.001000002
				.memorySize(1024) // increase memory size to 1 GB for JarHC
				.timeout(Duration.seconds(60)) // increase timeout to 60 seconds for JarHC
				.ephemeralStorageSize(Size.mebibytes(512)) // TODO: reduce ephemeral storage size?
				.initialPolicy(List.of(
						PolicyStatement.Builder.create()
								.actions(List.of("s3:*")) // TODO: limit to read/write access to "/reports/*"
								.resources(List.of(websiteBucket.getBucketArn(), websiteBucket.arnForObjects("*")))
								.build()
				))
				.deadLetterQueue(errorQueue)
				.environment(map(
						"BUCKET_NAME", websiteBucketName,
						"BUCKET_URL", websiteBucketUrl,
						// TODO: disable JAVA_TOOL_OPTIONS?
						"JAVA_TOOL_OPTIONS", "-XX:+TieredCompilation -XX:TieredStopAtLevel=1"
				))
				.build();

		EventInvokeConfig.Builder.create(this, "JarhcCheckFunctionEventInvokeConfig")
				.function(jarhcCheckFunction)
				.maxEventAge(Duration.minutes(2))
				.retryAttempts(0)
				// TODO: .qualifier("Live")
				.build();

		AssetImageCode japiccCheckDockerImage = AssetImageCode.Builder.create("../jarhc-aws-lambda")
				.build();

		IFunction japiccCheckFunction = Function.Builder.create(this, "JapiccCheckFunction")
				.functionName("japicc-check")
				// TODO: migrate to Amazon Linux 2 as base image?
				//  https://docs.aws.amazon.com/lambda/latest/dg/java-image.html
				.code(japiccCheckDockerImage)
				.handler(Handler.FROM_IMAGE)
				.runtime(Runtime.FROM_IMAGE)
				.tracing(Tracing.ACTIVE)
				// max price per execution:
				// memorySize * timeout / 1024 * $0.0000166667 = $0.001000002
				.memorySize(1024) // increase memory size to 1 GB for JAPICC
				.timeout(Duration.seconds(60)) // increase timeout to 60 seconds for JAPICC
				.ephemeralStorageSize(Size.mebibytes(512)) // TODO: reduce ephemeral storage size?
				.initialPolicy(List.of(
						PolicyStatement.Builder.create()
								.actions(List.of("s3:*")) // TODO: limit to read/write access to "/reports/*"
								.resources(List.of(websiteBucket.getBucketArn(), websiteBucket.arnForObjects("*")))
								.build()
				))
				.deadLetterQueue(errorQueue)
				.environment(map(
						"BUCKET_NAME", websiteBucketName,
						"BUCKET_URL", websiteBucketUrl
						// TODO: enable JAVA_TOOL_OPTIONS?
						// "JAVA_TOOL_OPTIONS", "-XX:+TieredCompilation -XX:TieredStopAtLevel=1"
				))
				.build();

		EventInvokeConfig.Builder.create(this, "JapiccCheckFunctionEventInvokeConfig")
				.function(japiccCheckFunction)
				.maxEventAge(Duration.minutes(2))
				.retryAttempts(0)
				// TODO: .qualifier("Live")
				.build();

		// TODO: add additional Lambda functions:
		//  - JAPICC function (based on Docker image)

		IFunction restApiFunction = Function.Builder.create(this, "RestApiFunction")
				.functionName("rest-api")
				.code(Code.fromAsset("../jarhc-aws-lambda/build/distributions/jarhc-aws-lambda-1.0.0.zip"))
				.handler("org.jarhc.online.rest.Handler")
				.runtime(Runtime.JAVA_11) // TODO: migrate to Java 17
				.architecture(Architecture.X86_64) // TODO: migrate to ARM64?
				// TODO: enable SnapStart
				.tracing(Tracing.ACTIVE)
				// max price per execution:
				// memorySize * timeout / 1024 * $0.0000166667 = $0.0000833335
				.memorySize(512) // increase memory size to 512 MB for REST API
				.timeout(Duration.seconds(10)) // increase timeout to 10 seconds for REST API
				.ephemeralStorageSize(Size.mebibytes(512)) // TODO: reduce ephemeral storage size?
				.initialPolicy(List.of(
						PolicyStatement.Builder.create()
								.actions(List.of("s3:*")) // TODO: limit to read access to "/reports/*"
								.resources(List.of(websiteBucket.getBucketArn(), websiteBucket.arnForObjects("*")))
								.build(),
						PolicyStatement.Builder.create()
								.actions(List.of("lambda:InvokeFunction"))
								.resources(List.of(jarhcCheckFunction.getFunctionArn()))
								.build(),
						PolicyStatement.Builder.create()
								.actions(List.of("lambda:InvokeFunction"))
								.resources(List.of(japiccCheckFunction.getFunctionArn()))
								.build()
				))
				.environment(map(
						"BUCKET_NAME", websiteBucketName,
						"BUCKET_URL", websiteBucketUrl,
						"JAVA_TOOL_OPTIONS", "-XX:+TieredCompilation -XX:TieredStopAtLevel=1"
				))
				.build();

		// TODO: create alias and version for REST API function?
		// Alias restApiAlias = Alias.Builder.create(this, "RestApiAlias")
		// 		.aliasName("Live")
		// 		.version(restApiFunction.getLatestVersion())
		// 		.build();

		LambdaIntegrationOptions lambdaIntegrationOptions = LambdaIntegrationOptions.builder()
				.allowTestInvoke(false)
				.timeout(Duration.seconds(10))
				// TODO:
				//  .credentialsPassthrough(true)
				//  ...
				.build();

		CorsOptions corsOptions = CorsOptions.builder()
				.allowOrigins(List.of(websiteUrl))
				.allowMethods(List.of("GET", "POST", "OPTIONS"))
				.allowHeaders(List.of("Content-Type", "Authorization"))
				.allowCredentials(true)
				.maxAge(Duration.minutes(10)) // cache results of a preflight request for 10 minutes
				.build();

		RestApi restApi = RestApi.Builder.create(this, "RestApi")
				.restApiName("RestApi")
				.description("API for JarHC Online")
				.domainName(
						DomainNameOptions.builder()
								.domainName(apiDomain)
								.certificate(apiCertificate)
								.build()
				)
				.deploy(true)
				.endpointConfiguration(
						EndpointConfiguration.builder()
								.types(List.of(EndpointType.REGIONAL))
								.build()
				)
				.minCompressionSize(Size.bytes(1024))
				// .defaultCorsPreflightOptions(corsOptions)
				// .defaultMethodOptions(
				// 		MethodOptions.builder()
				// 				// TODO
				// 				.build()
				// )
				.disableExecuteApiEndpoint(true)
				.deployOptions(
						StageOptions.builder()
								.stageName("Prod")
								//.documentationVersion("1")
								.description("Production stage")
								.cachingEnabled(false)
								.throttlingBurstLimit(20)
								.throttlingRateLimit(10)
								// TODO:
								//  .loggingLevel(MethodLoggingLevel.INFO)
								//  .tracingEnabled(true)
								//  .metricsEnabled(true)
								//  .dataTraceEnabled(true) // do not use in production
								//  .variables(...)
								//  .accessLogDestination(...)
								//  .accessLogFormat(...)
								.build()
				)
				.build();

		// request models ------------------------------------------------------

		JsonSchema coordinatesModel = JsonSchema.builder().type(JsonSchemaType.STRING).build(); // TODO: pattern: "^[^:]+:[^:]+:[^:]+$"
		JsonSchema coordinatesListModel = JsonSchema.builder().type(JsonSchemaType.ARRAY).items(coordinatesModel).build();

		Model japiccSubmitModel = restApi.addModel("japiccSubmitModel", ModelOptions.builder()
				.modelName("japiccSubmitModel")
				.description("")
				.contentType("application/json")
				.schema(
						JsonSchema.builder()
								.additionalProperties(false)
								.required(list("oldVersion", "newVersion"))
								.type(JsonSchemaType.OBJECT)
								.properties(map(
										"oldVersion", coordinatesModel,
										"newVersion", coordinatesModel
								))
								.build()
				)
				.build());

		Model jarhcSubmitModel = restApi.addModel("jarhcSubmitModel", ModelOptions.builder()
				.modelName("jarhcSubmitModel")
				.description("")
				.contentType("application/json")
				.schema(
						JsonSchema.builder()
								.additionalProperties(false)
								.required(list("classpath"))
								.type(JsonSchemaType.OBJECT)
								.properties(map(
										"provided", coordinatesListModel,
										"classpath", coordinatesListModel
								))
								.build()
				)
				.build());

		Model mavenSearchModel = restApi.addModel("mavenSearchModel", ModelOptions.builder()
				.modelName("mavenSearchModel")
				.description("")
				.contentType("application/json")
				.schema(
						JsonSchema.builder()
								.additionalProperties(false)
								.required(list("coordinates"))
								.type(JsonSchemaType.OBJECT)
								.properties(map(
										"coordinates", coordinatesModel)
								)
								.build()
				)
				.build());

		// default error responses ---------------------------------------------

		restApi.addGatewayResponse("DEFAULT_4XX", GatewayResponseOptions.builder()
				.type(ResponseType.DEFAULT_4_XX)
				.responseHeaders(map(
						"Access-Control-Allow-Origin", "'" + websiteUrl + "'",
						"Access-Control-Allow-Credentials", "'true'"
				))
				.templates(map())
				.build()
		);

		restApi.addGatewayResponse("DEFAULT_5XX", GatewayResponseOptions.builder()
				.type(ResponseType.DEFAULT_5_XX)
				.responseHeaders(map(
						"Access-Control-Allow-Origin", "'" + websiteUrl + "'",
						"Access-Control-Allow-Credentials", "'true'"
				))
				.templates(map())
				.build()
		);

		// resources -----------------------------------------------------------

		IAuthorizer cognitoAuthorizer = CognitoUserPoolsAuthorizer.Builder.create(this, "CognitoAuthorizer")
				.authorizerName("CognitoAuthorizer")
				.identitySource("method.request.header.Authorization")
				.cognitoUserPools(List.of(cognitoUserPool))
				.resultsCacheTtl(Duration.minutes(5))
				.build();

		Resource mavenSearchPath = restApi.getRoot()
				.addResource("maven")
				.addResource("search");
		mavenSearchPath.addCorsPreflight(corsOptions);
		mavenSearchPath
				.addMethod(
						"POST",
						new LambdaIntegration(restApiFunction, lambdaIntegrationOptions),
						MethodOptions.builder()
								.requestModels(map("application/json", mavenSearchModel))
								.build()
				);

		Resource authValidatePath = restApi.getRoot()
				.addResource("auth")
				.addResource("validate");
		authValidatePath.addCorsPreflight(corsOptions);
		authValidatePath
				.addMethod(
						"GET",
						new LambdaIntegration(restApiFunction, lambdaIntegrationOptions),
						MethodOptions.builder()
								.authorizer(cognitoAuthorizer)
								.build()
				);

		Resource japiccSubmitPath = restApi.getRoot()
				.addResource("japicc")
				.addResource("submit");
		japiccSubmitPath.addCorsPreflight(corsOptions);
		japiccSubmitPath
				.addMethod(
						"POST",
						new LambdaIntegration(restApiFunction, lambdaIntegrationOptions),
						MethodOptions.builder()
								.requestModels(map("application/json", japiccSubmitModel))
								.authorizer(cognitoAuthorizer)
								.build()
				);

		Resource jarhcSubmitPath = restApi.getRoot()
				.addResource("jarhc")
				.addResource("submit");
		jarhcSubmitPath.addCorsPreflight(corsOptions);
		jarhcSubmitPath
				.addMethod(
						"POST",
						new LambdaIntegration(restApiFunction, lambdaIntegrationOptions),
						MethodOptions.builder()
								.requestModels(map("application/json", jarhcSubmitModel))
								.authorizer(cognitoAuthorizer)
								.build()
				);

		// DNS record ----------------------------------------------------------

		// Route 53 DNS record for API Gateway domain
		ARecord.Builder.create(this, "ApiDnsRecord")
				.zone(hostedZone)
				.recordName(apiDomain)
				.target(RecordTarget.fromAlias(new ApiGatewayDomain(restApi.getDomainName())))
				.build();

	}

}
