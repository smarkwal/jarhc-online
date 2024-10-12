package com.myorg;

import software.constructs.Construct;

import java.util.*;
import software.amazon.awscdk.CfnMapping;
import software.amazon.awscdk.CfnTag;
import software.amazon.awscdk.Stack;
import software.amazon.awscdk.StackProps;

import software.amazon.awscdk.*;
import software.amazon.awscdk.services.cognito.*;
import software.amazon.awscdk.services.route53.*;

class JarhcOnlineCognitoStack extends Stack {
    private Object cognitoUserPoolId;

    private Object cognitoUserPoolProviderUrl;

    private Object cognitoAppClientId;

    public Object getCognitoUserPoolId() {
        return this.cognitoUserPoolId;
    }

    public Object getCognitoUserPoolProviderUrl() {
        return this.cognitoUserPoolProviderUrl;
    }

    public Object getCognitoAppClientId() {
        return this.cognitoAppClientId;
    }

    public JarhcOnlineCognitoStack(final Construct scope, final String id) {
        super(scope, id, null);
    }

    public JarhcOnlineCognitoStack(final Construct scope, final String id, final StackProps props) {
        this(scope, id, props, null, null, null, null);
    }

    public JarhcOnlineCognitoStack(final Construct scope, final String id, final StackProps props,
            String cognitoDomain,
            String cognitoCertificateArn,
            String emailArn,
            String dnsZoneId) {
        super(scope, id, props);

        cognitoDomain = Optional.ofNullable(cognitoDomain).isPresent() ? cognitoDomain
                : "login.jarhc.org";
        cognitoCertificateArn = Optional.ofNullable(cognitoCertificateArn).isPresent() ? cognitoCertificateArn
                : "arn:aws:acm:us-east-1:837783538267:certificate/26192609-2067-455f-b1c3-f67b75b15926";
        emailArn = Optional.ofNullable(emailArn).isPresent() ? emailArn
                : "arn:aws:ses:eu-west-1:837783538267:identity/do-not-reply@jarhc.org";
        dnsZoneId = Optional.ofNullable(dnsZoneId).isPresent() ? dnsZoneId
                : "Z39VOOPW73P7H0";

        CfnRecordSet cognitoDnsRecord = CfnRecordSet.Builder.create(this, "CognitoDnsRecord")
                .hostedZoneId(dnsZoneId)
                .name(cognitoDomain)
                .type("A")
                .aliasTarget(CfnRecordSet.AliasTargetProperty.builder()
                        .dnsName("d1h9aibn2whqxe.cloudfront.net")
                        .hostedZoneId("Z2FDTNDATAQYW2")
                        .evaluateTargetHealth(false)
                        .build())
                .build();

        cognitoDnsRecord.addMetadata("SamResourceId", "CognitoDnsRecord");

        CfnUserPool cognitoUserPool = CfnUserPool.Builder.create(this, "CognitoUserPool")
                .userPoolName("online.jarhc.org")
                .usernameAttributes(Arrays.asList(
                        "email"))
                .autoVerifiedAttributes(Arrays.asList(
                        "email"))
                .usernameConfiguration(CfnUserPool.UsernameConfigurationProperty.builder()
                        .caseSensitive(false)
                        .build())
                .mfaConfiguration("OPTIONAL")
                .enabledMfas(Arrays.asList(
                        "SOFTWARE_TOKEN_MFA"))
                .emailConfiguration(CfnUserPool.EmailConfigurationProperty.builder()
                        .emailSendingAccount("DEVELOPER")
                        .sourceArn(emailArn)
                        .from("JarHC <do-not-reply@jarhc.org>")
                        .build())
                .adminCreateUserConfig(CfnUserPool.AdminCreateUserConfigProperty.builder()
                        .allowAdminCreateUserOnly(false)
                        .unusedAccountValidityDays(7)
                        .build())
                .accountRecoverySetting(CfnUserPool.AccountRecoverySettingProperty.builder()
                        .recoveryMechanisms(Arrays.asList(
                                CfnUserPool.RecoveryOptionProperty.builder()
                                        .name("verified_email")
                                        .priority(1)
                                        .build()))
                        .build())
                .build();

        cognitoUserPool.addMetadata("SamResourceId", "CognitoUserPool");

        CfnUserPoolGroup cognitoAdminGroup = CfnUserPoolGroup.Builder.create(this, "CognitoAdminGroup")
                .userPoolId(cognitoUserPool.getRef())
                .groupName("Administrators")
                .precedence(0)
                .build();

        cognitoAdminGroup.addMetadata("SamResourceId", "CognitoAdminGroup");

        CfnUserPoolClient cognitoAppClient = CfnUserPoolClient.Builder.create(this, "CognitoAppClient")
                .userPoolId(cognitoUserPool.getRef())
                .clientName("online.jarhc.org")
                .generateSecret(true)
                .allowedOAuthFlowsUserPoolClient(true)
                .allowedOAuthFlows(Arrays.asList(
                        "code",
                        "implicit"))
                .allowedOAuthScopes(Arrays.asList(
                        "openid",
                        "profile",
                        "email",
                        "phone"))
                .defaultRedirectUri("https://online.jarhc.org/login.html")
                .callbackUrLs(Arrays.asList(
                        "https://online.jarhc.org/login.html",
                        "http://localhost:3000/login.html"))
                .logoutUrLs(Arrays.asList(
                        "https://online.jarhc.org/logout.html",
                        "http://localhost:3000/logout.html"))
                .idTokenValidity(3)
                .accessTokenValidity(3)
                .refreshTokenValidity(7)
                .enableTokenRevocation(true)
                .preventUserExistenceErrors("ENABLED")
                .supportedIdentityProviders(Arrays.asList(
                        "COGNITO"))
                .writeAttributes(Arrays.asList(
                        "name"))
                .build();

        cognitoAppClient.addMetadata("SamResourceId", "CognitoAppClient");

        CfnUserPoolDomain cognitoCustomDomain = CfnUserPoolDomain.Builder.create(this, "CognitoCustomDomain")
                .userPoolId(cognitoUserPool.getRef())
                .domain(cognitoDomain)
                .customDomainConfig(CfnUserPoolDomain.CustomDomainConfigTypeProperty.builder()
                        .certificateArn(cognitoCertificateArn)
                        .build())
                .build();

        cognitoCustomDomain.addMetadata("SamResourceId", "CognitoCustomDomain");

        this.cognitoUserPoolId = cognitoUserPool.getRef();
        CfnOutput.Builder.create(this, "CfnOutputCognitoUserPoolID")
                .key("CognitoUserPoolID")
                .value(this.cognitoUserPoolId.toString())
                .description("Cognito User Pool ID")
                .build();

        this.cognitoUserPoolProviderUrl = cognitoUserPool.getAttrProviderUrl();
        CfnOutput.Builder.create(this, "CfnOutputCognitoUserPoolProviderURL")
                .key("CognitoUserPoolProviderURL")
                .value(this.cognitoUserPoolProviderUrl.toString())
                .description("Cognito User Pool Provider URL")
                .build();

        this.cognitoAppClientId = cognitoAppClient.getRef();
        CfnOutput.Builder.create(this, "CfnOutputCognitoAppClientID")
                .key("CognitoAppClientID")
                .value(this.cognitoAppClientId.toString())
                .description("Cognito App Client ID")
                .build();

    }
}
