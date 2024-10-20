package org.jarhc.online.awscdk.stacks;

import software.amazon.awscdk.StackProps;
import software.amazon.awscdk.services.cloudfront.CfnDistribution;
import software.amazon.awscdk.services.cloudfront.CfnOriginAccessControl;
import software.amazon.awscdk.services.route53.CfnRecordSetGroup;
import software.amazon.awscdk.services.s3.CfnBucket;
import software.amazon.awscdk.services.s3.CfnBucketPolicy;
import software.constructs.Construct;

public class WebsiteStack extends AbstractStack {

	private static final String MANAGED_CACHING_DISABLED = "4135ea2d-6df8-44a3-9df3-4b5a84be39ad"; // Managed: CachingDisabled
	private static final String MANAGED_SECURITY_HEADER_POLICY = "67f7725c-6f97-4210-82d7-5512b31e9d03"; // Managed: SecurityHeadersPolicy

	public WebsiteStack(
			final Construct scope,
			final String id,
			final StackProps props,
			// configuration:
			final String websiteCertificateArn,
			final String websiteDomain,
			final String websiteLogsBucket,
			final String dnsZoneId
	) {
		super(scope, id, props);

		// S3 Bucket for public website
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
										list(
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
								.destinationBucketName("aws-cloud-reports") // TODO: replace with parameter
								.logFilePrefix("s3-access-logs/")
								.build()
				)
				.build();

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

		// CloudFront Distribution for public website
		CfnDistribution websiteCloudFrontDistribution = CfnDistribution.Builder.create(this, "WebsiteCloudFrontDistribution")
				.distributionConfig(
						CfnDistribution.DistributionConfigProperty.builder()
								.enabled(true)
								.aliases(list(websiteDomain))
								.httpVersion("http2")
								.origins(
										list(
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
												.allowedMethods(list("GET", "HEAD", "OPTIONS"))
												.cachedMethods(list("GET", "HEAD"))
												.cachePolicyId(MANAGED_CACHING_DISABLED)
												.responseHeadersPolicyId(MANAGED_SECURITY_HEADER_POLICY)
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

		// S3 Bucket Policy for public website
		CfnBucketPolicy.Builder.create(this, "WebsiteBucketPolicy")
				.bucket(websiteBucket.getRef())
				.policyDocument(map(
								"Version", "2012-10-17",
								"Statement", list(
										map(
												"Sid", "AllowCloudFrontServicePrincipal",
												"Effect", "Allow",
												"Principal", map(
														"Service", "cloudfront.amazonaws.com"
												),
												"Action", list(
														"s3:ListBucket",
														"s3:GetObject"
												),
												"Resource", list(
														"arn:aws:s3:::" + websiteBucket.getRef(),
														"arn:aws:s3:::" + websiteBucket.getRef() + "/*"
												),
												"Condition", map(
														"StringEquals", map(
																"AWS:SourceArn", "arn:aws:cloudfront::" + this.getAccount() + ":distribution/" + websiteCloudFrontDistribution.getAttrId()
														)
												)
										)
								)
						)
				)
				.build();

		// Route 53 DNS record for public website (CloudFront distribution)
		CfnRecordSetGroup.Builder.create(this, "WebsiteDnsRecord")
				.hostedZoneId(dnsZoneId)
				.recordSets(
						list(
								CfnRecordSetGroup.RecordSetProperty.builder()
										.name(websiteDomain)
										.type("A")
										.aliasTarget(
												CfnRecordSetGroup.AliasTargetProperty.builder()
														.dnsName(websiteCloudFrontDistribution.getAttrDomainName())
														.hostedZoneId(CLOUDFRONT_HOSTED_ZONE_ID)
														.evaluateTargetHealth(false)
														.build()
										)
										.build()
						)
				)
				.build();

		createOutput(
				"WebsiteCloudFrontDistributionUrl",
				"https://" + websiteCloudFrontDistribution.getAttrDomainName() + "/",
				"Public URL of CloudFront Distribution."
		);

	}
}
