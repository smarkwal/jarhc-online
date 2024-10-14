package org.jarhc.online.awscdk;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import software.amazon.awscdk.CfnOutput;
import software.amazon.awscdk.Stack;
import software.amazon.awscdk.StackProps;
import software.amazon.awscdk.services.cloudfront.CfnDistribution;
import software.amazon.awscdk.services.cloudfront.CfnOriginAccessControl;
import software.amazon.awscdk.services.route53.CfnRecordSetGroup;
import software.amazon.awscdk.services.s3.CfnBucket;
import software.amazon.awscdk.services.s3.CfnBucketPolicy;
import software.constructs.Construct;

class WebsiteStack extends Stack {

	private Object websiteCloudFrontDistributionUrl;

	public Object getWebsiteCloudFrontDistributionUrl() {
		return this.websiteCloudFrontDistributionUrl;
	}

	public WebsiteStack(final Construct scope, final String id) {
		super(scope, id, null);
	}

	public WebsiteStack(final Construct scope, final String id, final StackProps props) {
		this(scope, id, props, null, null, null, null);
	}

	public WebsiteStack(final Construct scope, final String id, final StackProps props,
	                    String websiteDomain,
	                    String websiteCertificateArn,
	                    String websiteLogsBucket,
	                    String dnsZoneId) {
		super(scope, id, props);

		websiteDomain = Optional.ofNullable(websiteDomain).isPresent() ? websiteDomain : "online.jarhc.org";
		websiteCertificateArn = Optional.ofNullable(websiteCertificateArn).isPresent() ? websiteCertificateArn : "arn:aws:acm:us-east-1:837783538267:certificate/3fa0002b-0667-4e67-b8c5-d228a4341811";
		websiteLogsBucket = Optional.ofNullable(websiteLogsBucket).isPresent() ? websiteLogsBucket : "aws-cloud-reports.s3.amazonaws.com";
		dnsZoneId = Optional.ofNullable(dnsZoneId).isPresent() ? dnsZoneId : "Z39VOOPW73P7H0";

		CfnBucket websiteBucket = CfnBucket.Builder.create(this, "WebsiteBucket")
				.bucketName(websiteDomain)
				.publicAccessBlockConfiguration(
						CfnBucket.PublicAccessBlockConfigurationProperty.builder()
								.blockPublicAcls(true)
								.blockPublicPolicy(true)
								.ignorePublicAcls(true)
								.restrictPublicBuckets(true)
								.build()
				)
				.lifecycleConfiguration(
						CfnBucket.LifecycleConfigurationProperty.builder()
								.rules(
										List.of(
												CfnBucket.RuleProperty.builder()
														.id("AutoCleanup")
														.status("Enabled")
														.prefix("reports/")
														.expirationInDays(30)
														.build()
										)
								)
								.build()
				)
				.loggingConfiguration(
						CfnBucket.LoggingConfigurationProperty.builder()
								.destinationBucketName("aws-cloud-reports")
								.logFilePrefix("s3-access-logs/")
								.build()
				)
				.build();

		websiteBucket.addMetadata("SamResourceId", "WebsiteBucket");

		CfnOriginAccessControl websiteOriginAccessControl = CfnOriginAccessControl.Builder.create(this, "WebsiteOriginAccessControl")
				.originAccessControlConfig(
						CfnOriginAccessControl.OriginAccessControlConfigProperty.builder()
								.name("WebsiteOriginAccessControl")
								.originAccessControlOriginType("s3")
								.signingBehavior("always")
								.signingProtocol("sigv4")
								.build()
				)
				.build();

		websiteOriginAccessControl.addMetadata("SamResourceId", "WebsiteOriginAccessControl");

		CfnDistribution websiteCloudFrontDistribution = CfnDistribution.Builder.create(this, "WebsiteCloudFrontDistribution")
				.distributionConfig(
						CfnDistribution.DistributionConfigProperty.builder()
								.enabled(true)
								.aliases(List.of(websiteDomain))
								.httpVersion("http2")
								.origins(
										List.of(
												CfnDistribution.OriginProperty.builder()
														.id("DefaultOrigin")
														.domainName(websiteBucket.getAttrRegionalDomainName())
														.originAccessControlId(websiteOriginAccessControl.getAttrId())
														.s3OriginConfig(
																CfnDistribution.S3OriginConfigProperty.builder()
																		.originAccessIdentity("")
																		.build()
														)
														.build()
										)
								)
								.defaultRootObject("index.html")
								.priceClass("PriceClass_100")
								.viewerCertificate(
										CfnDistribution.ViewerCertificateProperty.builder()
												.acmCertificateArn(websiteCertificateArn)
												.minimumProtocolVersion("TLSv1.2_2021")
												.sslSupportMethod("sni-only")
												.build()
								)
								.defaultCacheBehavior(
										CfnDistribution.DefaultCacheBehaviorProperty.builder()
												.allowedMethods(List.of("GET", "HEAD", "OPTIONS"))
												.cachedMethods(List.of("GET", "HEAD"))
												.cachePolicyId("4135ea2d-6df8-44a3-9df3-4b5a84be39ad")
												.responseHeadersPolicyId("67f7725c-6f97-4210-82d7-5512b31e9d03")
												.compress(true)
												.targetOriginId("DefaultOrigin")
												.viewerProtocolPolicy("redirect-to-https")
												.build()
								)
								.logging(
										CfnDistribution.LoggingProperty.builder()
												.bucket(websiteLogsBucket)
												.prefix("cloudfront-access-logs/")
												.includeCookies(false)
												.build()
								)
								.build()
				)
				.build();

		websiteCloudFrontDistribution.addMetadata("SamResourceId", "WebsiteCloudFrontDistribution");

		CfnBucketPolicy websiteBucketPolicy = CfnBucketPolicy.Builder.create(this, "WebsiteBucketPolicy")
				.bucket(websiteBucket.getRef())
				.policyDocument(Map.of(
								"Version", "2012-10-17",
								"Statement", List.of(
										Map.of(
												"Sid", "AllowCloudFrontServicePrincipal",
												"Effect", "Allow",
												"Principal", Map.of(
														"Service", "cloudfront.amazonaws.com"
												),
												"Action", List.of(
														"s3:ListBucket",
														"s3:GetObject"
												),
												"Resource", List.of(
														"arn:aws:s3:::" + websiteBucket.getRef(),
														"arn:aws:s3:::" + websiteBucket.getRef() + "/*"
												),
												"Condition", Map.of(
														"StringEquals", Map.of(
																"AWS:SourceArn", "arn:aws:cloudfront::" + this.getAccount() + ":distribution/" + websiteCloudFrontDistribution.getAttrId()
														)
												)
										)
								)
						)
				)
				.build();

		websiteBucketPolicy.addMetadata("SamResourceId", "WebsiteBucketPolicy");

		CfnRecordSetGroup websiteDnsRecord = CfnRecordSetGroup.Builder.create(this, "WebsiteDnsRecord")
				.hostedZoneId(dnsZoneId)
				.recordSets(
						List.of(
								CfnRecordSetGroup.RecordSetProperty.builder()
										.name(websiteDomain)
										.type("A")
										.aliasTarget(
												CfnRecordSetGroup.AliasTargetProperty.builder()
														.dnsName(websiteCloudFrontDistribution.getAttrDomainName())
														.hostedZoneId("Z2FDTNDATAQYW2")
														.evaluateTargetHealth(false)
														.build()
										)
										.build()
						)
				)
				.build();

		websiteDnsRecord.addMetadata("SamResourceId", "WebsiteDnsRecord");

		this.websiteCloudFrontDistributionUrl = "https://" + websiteCloudFrontDistribution.getAttrDomainName() + "/";
		CfnOutput.Builder.create(this, "CfnOutputWebsiteCloudFrontDistributionUrl")
				.key("WebsiteCloudFrontDistributionUrl")
				.value(this.websiteCloudFrontDistributionUrl.toString())
				.description("Public URL of CloudFront Distribution.")
				.build();

	}
}
