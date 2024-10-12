package com.myorg;

import software.constructs.Construct;

import java.util.*;
import software.amazon.awscdk.CfnMapping;
import software.amazon.awscdk.CfnTag;
import software.amazon.awscdk.Stack;
import software.amazon.awscdk.StackProps;

import software.amazon.awscdk.*;
import software.amazon.awscdk.services.s3.*;

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
                .publicAccessBlockConfiguration(CfnBucket.PublicAccessBlockConfigurationProperty.builder()
                        .blockPublicPolicy(true)
                        .blockPublicAcls(true)
                        .ignorePublicAcls(true)
                        .restrictPublicBuckets(true)
                        .build())
                .bucketEncryption(CfnBucket.BucketEncryptionProperty.builder()
                        .serverSideEncryptionConfiguration(Arrays.asList(
                                CfnBucket.ServerSideEncryptionRuleProperty.builder()
                                        .serverSideEncryptionByDefault(CfnBucket.ServerSideEncryptionByDefaultProperty.builder()
                                                .sseAlgorithm("aws:kms")
                                                .build())
                                        .build()))
                        .build())
                .versioningConfiguration(CfnBucket.VersioningConfigurationProperty.builder()
                        .status("Enabled")
                        .build())
                .tags(Arrays.asList(
                        CfnTag.builder()
                                .key("ManagedStackSource")
                                .value("AwsSamCli")
                                .build()))
                .build();

        CfnBucketPolicy samCliSourceBucketBucketPolicy = CfnBucketPolicy.Builder.create(this, "SamCliSourceBucketBucketPolicy")
                .bucket(samCliSourceBucket.getRef())
                .policyDocument(Map.of("Statement", Arrays.asList(
                        Map.of("Action", Arrays.asList(
                                "s3:GetObject"),
                        "Effect", "Allow",
                        "Resource", String.join("",
                                "arn:",
                                this.getPartition(),
                                ":s3:::",
                                samCliSourceBucket.getRef(),
                                "/*"),
                        "Principal", Map.of("Service", "serverlessrepo.amazonaws.com"),
                        "Condition", Map.of("StringEquals", Map.of("aws:SourceAccount", this.getAccount()))),
                        Map.of("Action", Arrays.asList(
                                "s3:*"),
                        "Effect", "Deny",
                        "Resource", Arrays.asList(
                                String.join("",
                                        "arn:",
                                        this.getPartition(),
                                        ":s3:::",
                                        samCliSourceBucket.getRef()),
                                String.join("",
                                        "arn:",
                                        this.getPartition(),
                                        ":s3:::",
                                        samCliSourceBucket.getRef(),
                                        "/*")),
                        "Principal", "*",
                        "Condition", Map.of("Bool", Map.of("aws:SecureTransport", "false"))))))
                .build();

        this.sourceBucket = samCliSourceBucket.getRef();
        CfnOutput.Builder.create(this, "CfnOutputSourceBucket")
                .key("SourceBucket")
                .value(this.sourceBucket.toString())
                .build();

    }
}
