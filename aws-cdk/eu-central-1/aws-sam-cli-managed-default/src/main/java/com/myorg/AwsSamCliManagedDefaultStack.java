package com.myorg;

import java.util.List;
import java.util.Map;
import software.amazon.awscdk.CfnOutput;
import software.amazon.awscdk.CfnTag;
import software.amazon.awscdk.Stack;
import software.amazon.awscdk.StackProps;
import software.amazon.awscdk.services.s3.CfnBucket;
import software.amazon.awscdk.services.s3.CfnBucketPolicy;
import software.constructs.Construct;

class AwsSamCliManagedDefaultStack extends Stack {

	private Object sourceBucket;

	public Object getSourceBucket() {
		return this.sourceBucket;
	}

	public AwsSamCliManagedDefaultStack(final Construct scope, final String id) {
		super(scope, id, null);
	}

	public AwsSamCliManagedDefaultStack(final Construct scope, final String id, final StackProps props) {
		super(scope, id, props);

		CfnBucket samCliSourceBucket = CfnBucket.Builder.create(this, "SamCliSourceBucket")
				.publicAccessBlockConfiguration(
						CfnBucket.PublicAccessBlockConfigurationProperty.builder()
								.blockPublicPolicy(true)
								.blockPublicAcls(true)
								.ignorePublicAcls(true)
								.restrictPublicBuckets(true)
								.build()
				)
				.bucketEncryption(
						CfnBucket.BucketEncryptionProperty.builder()
								.serverSideEncryptionConfiguration(
										List.of(
												CfnBucket.ServerSideEncryptionRuleProperty.builder()
														.serverSideEncryptionByDefault(
																CfnBucket.ServerSideEncryptionByDefaultProperty.builder()
																		.sseAlgorithm("aws:kms")
																		.build()
														)
														.build()
										)
								)
								.build()
				)
				.versioningConfiguration(
						CfnBucket.VersioningConfigurationProperty.builder()
								.status("Enabled")
								.build()
				)
				.tags(
						List.of(
								CfnTag.builder()
										.key("ManagedStackSource")
										.value("AwsSamCli")
										.build()
						)
				)
				.build();

		CfnBucketPolicy samCliSourceBucketBucketPolicy = CfnBucketPolicy.Builder.create(this, "SamCliSourceBucketBucketPolicy")
				.bucket(samCliSourceBucket.getRef())
				.policyDocument(
						Map.of(
								"Statement", List.of(
										Map.of(
												"Action", List.of("s3:GetObject"),
												"Effect", "Allow",
												"Resource", String.join("", "arn:", this.getPartition(), ":s3:::", samCliSourceBucket.getRef(), "/*"),
												"Principal", Map.of(
														"Service", "serverlessrepo.amazonaws.com"
												),
												"Condition", Map.of(
														"StringEquals", Map.of(
																"aws:SourceAccount", this.getAccount()
														)
												)
										),
										Map.of(
												"Action", List.of("s3:*"),
												"Effect", "Deny",
												"Resource", List.of(
														String.join("", "arn:", this.getPartition(), ":s3:::", samCliSourceBucket.getRef()),
														String.join("", "arn:", this.getPartition(), ":s3:::", samCliSourceBucket.getRef(), "/*")
												),
												"Principal", "*",
												"Condition", Map.of(
														"Bool", Map.of(
																"aws:SecureTransport", "false"
														)
												)
										)
								)
						)
				)
				.build();

		this.sourceBucket = samCliSourceBucket.getRef();
		CfnOutput.Builder.create(this, "CfnOutputSourceBucket")
				.key("SourceBucket")
				.value(this.sourceBucket.toString())
				.build();

	}
}
