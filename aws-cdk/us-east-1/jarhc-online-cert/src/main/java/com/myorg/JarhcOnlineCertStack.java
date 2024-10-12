package com.myorg;

import software.constructs.Construct;

import java.util.*;
import software.amazon.awscdk.CfnMapping;
import software.amazon.awscdk.CfnTag;
import software.amazon.awscdk.Stack;
import software.amazon.awscdk.StackProps;

import software.amazon.awscdk.*;
import software.amazon.awscdk.services.certificatemanager.*;

class JarhcOnlineCertStack extends Stack {
    private Object websiteCertificateArn;

    private Object cognitoCertificateArn;

    public Object getWebsiteCertificateArn() {
        return this.websiteCertificateArn;
    }

    public Object getCognitoCertificateArn() {
        return this.cognitoCertificateArn;
    }

    public JarhcOnlineCertStack(final Construct scope, final String id) {
        super(scope, id, null);
    }

    public JarhcOnlineCertStack(final Construct scope, final String id, final StackProps props) {
        this(scope, id, props, null, null, null);
    }

    public JarhcOnlineCertStack(final Construct scope, final String id, final StackProps props,
            String websiteDomain,
            String cognitoDomain,
            String dnsZoneId) {
        super(scope, id, props);

        websiteDomain = Optional.ofNullable(websiteDomain).isPresent() ? websiteDomain
                : "online.jarhc.org";
        cognitoDomain = Optional.ofNullable(cognitoDomain).isPresent() ? cognitoDomain
                : "login.jarhc.org";
        dnsZoneId = Optional.ofNullable(dnsZoneId).isPresent() ? dnsZoneId
                : "Z39VOOPW73P7H0";

        CfnCertificate cognitoCertificate = CfnCertificate.Builder.create(this, "CognitoCertificate")
                .domainName(cognitoDomain)
                .validationMethod("DNS")
                .domainValidationOptions(Arrays.asList(
                        CfnCertificate.DomainValidationOptionProperty.builder()
                                .domainName(cognitoDomain)
                                .hostedZoneId(dnsZoneId)
                                .build()))
                .build();

        cognitoCertificate.addMetadata("SamResourceId", "CognitoCertificate");

        CfnCertificate websiteCertificate = CfnCertificate.Builder.create(this, "WebsiteCertificate")
                .domainName(websiteDomain)
                .validationMethod("DNS")
                .domainValidationOptions(Arrays.asList(
                        CfnCertificate.DomainValidationOptionProperty.builder()
                                .domainName(websiteDomain)
                                .hostedZoneId(dnsZoneId)
                                .build()))
                .build();

        websiteCertificate.addMetadata("SamResourceId", "WebsiteCertificate");

        this.websiteCertificateArn = websiteCertificate.getRef();
        CfnOutput.Builder.create(this, "CfnOutputWebsiteCertificateARN")
                .key("WebsiteCertificateARN")
                .value(this.websiteCertificateArn.toString())
                .description("Website Certificate ARN")
                .build();

        this.cognitoCertificateArn = cognitoCertificate.getRef();
        CfnOutput.Builder.create(this, "CfnOutputCognitoCertificateARN")
                .key("CognitoCertificateARN")
                .value(this.cognitoCertificateArn.toString())
                .description("Cognito Certificate ARN")
                .build();

    }
}
