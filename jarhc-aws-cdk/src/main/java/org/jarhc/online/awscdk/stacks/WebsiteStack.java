package org.jarhc.online.awscdk.stacks;

import software.amazon.awscdk.Duration;
import software.amazon.awscdk.RemovalPolicy;
import software.amazon.awscdk.StackProps;
import software.amazon.awscdk.services.certificatemanager.Certificate;
import software.amazon.awscdk.services.certificatemanager.ICertificate;
import software.amazon.awscdk.services.cloudfront.AllowedMethods;
import software.amazon.awscdk.services.cloudfront.BehaviorOptions;
import software.amazon.awscdk.services.cloudfront.CachePolicy;
import software.amazon.awscdk.services.cloudfront.CachedMethods;
import software.amazon.awscdk.services.cloudfront.Distribution;
import software.amazon.awscdk.services.cloudfront.HttpVersion;
import software.amazon.awscdk.services.cloudfront.IOrigin;
import software.amazon.awscdk.services.cloudfront.PriceClass;
import software.amazon.awscdk.services.cloudfront.ResponseHeadersPolicy;
import software.amazon.awscdk.services.cloudfront.S3OriginAccessControl;
import software.amazon.awscdk.services.cloudfront.SSLMethod;
import software.amazon.awscdk.services.cloudfront.SecurityPolicyProtocol;
import software.amazon.awscdk.services.cloudfront.Signing;
import software.amazon.awscdk.services.cloudfront.ViewerProtocolPolicy;
import software.amazon.awscdk.services.cloudfront.origins.S3BucketOrigin;
import software.amazon.awscdk.services.cloudfront.origins.S3BucketOriginWithOACProps;
import software.amazon.awscdk.services.route53.ARecord;
import software.amazon.awscdk.services.route53.HostedZone;
import software.amazon.awscdk.services.route53.HostedZoneProviderProps;
import software.amazon.awscdk.services.route53.IHostedZone;
import software.amazon.awscdk.services.route53.RecordTarget;
import software.amazon.awscdk.services.route53.targets.CloudFrontTarget;
import software.amazon.awscdk.services.s3.BlockPublicAccess;
import software.amazon.awscdk.services.s3.Bucket;
import software.amazon.awscdk.services.s3.BucketAttributes;
import software.amazon.awscdk.services.s3.IBucket;
import software.amazon.awscdk.services.s3.LifecycleRule;
import software.constructs.Construct;

public class WebsiteStack extends AbstractStack {

	public WebsiteStack(
			final Construct scope,
			final String id,
			final StackProps props,
			// configuration:
			final String websiteCertificateArn,
			final String websiteDomain,
			final String websiteLogsBucketName,
			final String rootDomain
	) {
		super(scope, id, props);

		// find existing S3 Bucket for website logs
		IBucket websiteLogsBucket = Bucket.fromBucketAttributes(this, "WebsiteLogsBucket", BucketAttributes.builder().bucketName(websiteLogsBucketName).build());

		// reference to Route 53 hosted zone for root domain
		IHostedZone hostedZone = HostedZone.fromLookup(this, "HostedZone", HostedZoneProviderProps.builder().domainName(rootDomain).build());

		// reference to SSL certificate for CloudFront custom domain
		ICertificate websiteCertificate = Certificate.fromCertificateArn(this, "CognitoCertificate", websiteCertificateArn);

		// S3 Bucket for public website
		Bucket websiteBucket = Bucket.Builder.create(this, "WebsiteBucket")
				.bucketName(websiteDomain)
				.blockPublicAccess(BlockPublicAccess.BLOCK_ALL)
				.lifecycleRules(list(
						LifecycleRule.builder()
								.id("AutoCleanup")
								.enabled(true)
								.prefix("reports/")
								.expiration(Duration.days(30))
								.build()
				))
				.serverAccessLogsBucket(websiteLogsBucket)
				.serverAccessLogsPrefix("s3-access-logs/")
				.removalPolicy(RemovalPolicy.DESTROY)
				.build();

		S3OriginAccessControl websiteOriginAccessControl = S3OriginAccessControl.Builder.create(this, "WebsiteOriginAccessControl")
				.originAccessControlName("WebsiteOriginAccessControl")
				.signing(Signing.SIGV4_ALWAYS)
				.build();

		IOrigin websiteBucketOrigin = S3BucketOrigin.withOriginAccessControl(websiteBucket,
				S3BucketOriginWithOACProps.builder()
						.originId("DefaultOrigin")
						.originAccessControl(websiteOriginAccessControl)
						.build()
		);

		// CloudFront Distribution for public website
		Distribution websiteCloudFrontDistribution = Distribution.Builder.create(this, "WebsiteCloudFrontDistribution")
				.enabled(true)
				.domainNames(list(websiteDomain))
				.defaultBehavior(
						BehaviorOptions.builder()
								.origin(websiteBucketOrigin)
								.allowedMethods(AllowedMethods.ALLOW_GET_HEAD_OPTIONS)
								.cachedMethods(CachedMethods.CACHE_GET_HEAD)
								.cachePolicy(CachePolicy.CACHING_DISABLED)
								.responseHeadersPolicy(ResponseHeadersPolicy.SECURITY_HEADERS)
								.compress(true)
								.viewerProtocolPolicy(ViewerProtocolPolicy.REDIRECT_TO_HTTPS)
								.build()
				)
				.defaultRootObject("index.html")
				.httpVersion(HttpVersion.HTTP2)
				.priceClass(PriceClass.PRICE_CLASS_100)
				.enableLogging(true)
				.logFilePrefix("cloudfront-access-logs/")
				.logBucket(websiteLogsBucket)
				.logIncludesCookies(false)
				.certificate(websiteCertificate)
				.minimumProtocolVersion(SecurityPolicyProtocol.TLS_V1_2_2021)
				.sslSupportMethod(SSLMethod.SNI)
				.build();

		// Route 53 DNS record for public website (CloudFront Distribution)
		ARecord.Builder.create(this, "WebsiteDnsRecord")
				.zone(hostedZone)
				.recordName(websiteDomain)
				.target(RecordTarget.fromAlias(new CloudFrontTarget(websiteCloudFrontDistribution)))
				.build();

	}
}
