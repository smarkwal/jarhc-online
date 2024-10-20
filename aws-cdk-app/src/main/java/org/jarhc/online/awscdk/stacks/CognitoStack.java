package org.jarhc.online.awscdk.stacks;

import software.amazon.awscdk.StackProps;
import software.amazon.awscdk.services.cognito.CfnUserPool;
import software.amazon.awscdk.services.cognito.CfnUserPoolClient;
import software.amazon.awscdk.services.cognito.CfnUserPoolDomain;
import software.amazon.awscdk.services.cognito.CfnUserPoolGroup;
import software.amazon.awscdk.services.route53.CfnRecordSet;
import software.constructs.Construct;

public class CognitoStack extends AbstractStack {

	public CognitoStack(
			final Construct scope,
			final String id,
			final StackProps props,
			// configuration:
			final String cognitoCertificateArn,
			final String cognitoDomain,
			final String websiteDomain,
			final String emailArn,
			final String emailAddress,
			final String dnsZoneId
	) {
		super(scope, id, props);

		// DNS record for Cognito custom domain
		CfnRecordSet.Builder.create(this, "CognitoDnsRecord")
				.hostedZoneId(dnsZoneId)
				.name(cognitoDomain)
				.type("A")
				.aliasTarget(
						CfnRecordSet.AliasTargetProperty.builder()
								.dnsName("d1h9aibn2whqxe.cloudfront.net") // TODO: how to get the DNS name for CloudFront distribution created for Cognito?
								.hostedZoneId(CLOUDFRONT_HOSTED_ZONE_ID)
								.evaluateTargetHealth(false)
								.build()
				)
				.build();

		// Cognito user pool
		CfnUserPool cognitoUserPool = CfnUserPool.Builder.create(this, "CognitoUserPool")
				.userPoolName(websiteDomain)
				.usernameAttributes(list("email"))
				.autoVerifiedAttributes(list("email"))
				.usernameConfiguration(CfnUserPool.UsernameConfigurationProperty.builder()
						.caseSensitive(false)
						.build())
				.mfaConfiguration("OPTIONAL")
				.enabledMfas(list("SOFTWARE_TOKEN_MFA"))
				.emailConfiguration(CfnUserPool.EmailConfigurationProperty.builder()
						.emailSendingAccount("DEVELOPER")
						.sourceArn(emailArn)
						.from("JarHC <" + emailAddress + ">")
						.build())
				.adminCreateUserConfig(CfnUserPool.AdminCreateUserConfigProperty.builder()
						.allowAdminCreateUserOnly(false)
						.unusedAccountValidityDays(7)
						.build())
				.accountRecoverySetting(CfnUserPool.AccountRecoverySettingProperty.builder()
						.recoveryMechanisms(list(
								CfnUserPool.RecoveryOptionProperty.builder()
										.name("verified_email")
										.priority(1)
										.build()))
						.build())
				.build();

		// Cognito user group "Administrators"
		CfnUserPoolGroup.Builder.create(this, "CognitoAdminGroup")
				.userPoolId(cognitoUserPool.getRef())
				.groupName("Administrators")
				.precedence(0)
				.build();

		// ognito app client used by public website and API gateway
		CfnUserPoolClient cognitoAppClient = CfnUserPoolClient.Builder.create(this, "CognitoAppClient")
				.userPoolId(cognitoUserPool.getRef())
				.clientName(websiteDomain)
				.generateSecret(true)
				.allowedOAuthFlowsUserPoolClient(true)
				.allowedOAuthFlows(list("code", "implicit"))
				.allowedOAuthScopes(list("openid", "profile", "email", "phone"))
				.defaultRedirectUri("https://" + websiteDomain + "/login.html")
				.callbackUrLs(list("https://" + websiteDomain + "/login.html", "http://localhost:3000/login.html"))
				.logoutUrLs(list("https://" + websiteDomain + "/logout.html", "http://localhost:3000/logout.html"))
				.idTokenValidity(3)
				.accessTokenValidity(3)
				.refreshTokenValidity(7)
				.enableTokenRevocation(true)
				.preventUserExistenceErrors("ENABLED")
				.supportedIdentityProviders(list("COGNITO"))
				.writeAttributes(list("name"))
				.build();

		// Cognito custom domain
		CfnUserPoolDomain.Builder.create(this, "CognitoCustomDomain")
				.userPoolId(cognitoUserPool.getRef())
				.domain(cognitoDomain)
				.customDomainConfig(
						CfnUserPoolDomain.CustomDomainConfigTypeProperty.builder()
								.certificateArn(cognitoCertificateArn)
								.build()
				)
				.build();

		createOutput(
				"CognitoClientID",
				cognitoAppClient.getRef(),
				"Cognito App Client ID"
		);
		createOutput(
				"CognitoUserPoolARN",
				cognitoUserPool.getAttrArn(),
				"Cognito User Pool ARN"
		);

	}

}
