package org.jarhc.online.awscdk.stacks;

import software.amazon.awscdk.StackProps;
import software.amazon.awscdk.services.certificatemanager.Certificate;
import software.amazon.awscdk.services.certificatemanager.CertificateValidation;
import software.amazon.awscdk.services.route53.HostedZone;
import software.amazon.awscdk.services.route53.HostedZoneProviderProps;
import software.amazon.awscdk.services.route53.IHostedZone;
import software.constructs.Construct;

public class CertStack extends AbstractStack {

	public CertStack(
			final Construct scope,
			final String id,
			final StackProps props,
			// configuration:
			final String websiteDomain,
			final String cognitoDomain,
			final String rootDomain
	) {
		super(scope, id, props);

		// reference to Route 53 hosted zone for root domain
		IHostedZone hostedZone = HostedZone.fromLookup(this, "HostedZone", HostedZoneProviderProps.builder().domainName(rootDomain).build());

		// SSL certificate for custom domain used by Cognito for user authentication
		Certificate cognitoCertificate = Certificate.Builder.create(this, "CognitoCertificate")
				.domainName(cognitoDomain)
				.validation(CertificateValidation.fromDns(hostedZone))
				.build();

		// SSL certificate for custom domain used by S3 for the static website
		Certificate websiteCertificate = Certificate.Builder.create(this, "WebsiteCertificate")
				.domainName(websiteDomain)
				.validation(CertificateValidation.fromDns(hostedZone))
				.build();

		// output certificate ARNs
		// (used in the other stacks to configure CloudFront distributions)
		createOutput(
				"WebsiteCertificateARN",
				websiteCertificate.getCertificateArn(),
				"Website Certificate ARN"
		);
		createOutput(
				"CognitoCertificateARN",
				cognitoCertificate.getCertificateArn(),
				"Cognito Certificate ARN"
		);

	}
}
