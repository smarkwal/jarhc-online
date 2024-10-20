package org.jarhc.online.awscdk.stacks;

import software.amazon.awscdk.StackProps;
import software.amazon.awscdk.services.certificatemanager.CfnCertificate;
import software.constructs.Construct;

public class CertStack extends AbstractStack {

	public CertStack(
			final Construct scope,
			final String id,
			final StackProps props,
			// configuration:
			final String websiteDomain,
			final String cognitoDomain,
			final String dnsZoneId
	) {
		super(scope, id, props);

		CfnCertificate cognitoCertificate = CfnCertificate.Builder.create(this, "CognitoCertificate")
				.domainName(cognitoDomain)
				.validationMethod("DNS")
				.domainValidationOptions(
						list(
								CfnCertificate.DomainValidationOptionProperty.builder()
										.domainName(cognitoDomain)
										.hostedZoneId(dnsZoneId)
										.build()
						)
				)
				.build();

		CfnCertificate websiteCertificate = CfnCertificate.Builder.create(this, "WebsiteCertificate")
				.domainName(websiteDomain)
				.validationMethod("DNS")
				.domainValidationOptions(
						list(
								CfnCertificate.DomainValidationOptionProperty.builder()
										.domainName(websiteDomain)
										.hostedZoneId(dnsZoneId)
										.build()
						)
				)
				.build();

		createOutput(
				"WebsiteCertificateARN",
				websiteCertificate.getRef(),
				"Website Certificate ARN"
		);
		createOutput(
				"CognitoCertificateARN",
				cognitoCertificate.getRef(),
				"Cognito Certificate ARN"
		);

	}
}
