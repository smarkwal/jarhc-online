package org.jarhc.online.awscdk.stacks;

import software.amazon.awscdk.Duration;
import software.amazon.awscdk.RemovalPolicy;
import software.amazon.awscdk.StackProps;
import software.amazon.awscdk.services.certificatemanager.Certificate;
import software.amazon.awscdk.services.certificatemanager.ICertificate;
import software.amazon.awscdk.services.cognito.AccountRecovery;
import software.amazon.awscdk.services.cognito.AutoVerifiedAttrs;
import software.amazon.awscdk.services.cognito.CustomDomainOptions;
import software.amazon.awscdk.services.cognito.Mfa;
import software.amazon.awscdk.services.cognito.MfaSecondFactor;
import software.amazon.awscdk.services.cognito.OAuthFlows;
import software.amazon.awscdk.services.cognito.OAuthScope;
import software.amazon.awscdk.services.cognito.OAuthSettings;
import software.amazon.awscdk.services.cognito.PasswordPolicy;
import software.amazon.awscdk.services.cognito.SignInAliases;
import software.amazon.awscdk.services.cognito.UserPool;
import software.amazon.awscdk.services.cognito.UserPoolClient;
import software.amazon.awscdk.services.cognito.UserPoolClientIdentityProvider;
import software.amazon.awscdk.services.cognito.UserPoolClientOptions;
import software.amazon.awscdk.services.cognito.UserPoolDomain;
import software.amazon.awscdk.services.cognito.UserPoolDomainOptions;
import software.amazon.awscdk.services.cognito.UserPoolEmail;
import software.amazon.awscdk.services.cognito.UserPoolSESOptions;
import software.amazon.awscdk.services.route53.ARecord;
import software.amazon.awscdk.services.route53.HostedZone;
import software.amazon.awscdk.services.route53.HostedZoneProviderProps;
import software.amazon.awscdk.services.route53.IHostedZone;
import software.amazon.awscdk.services.route53.RecordTarget;
import software.amazon.awscdk.services.route53.targets.UserPoolDomainTarget;
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
			final String emailAddress,
			final String rootDomain
	) {
		super(scope, id, props);

		// Cognito user pool
		UserPool cognitoUserPool = UserPool.Builder.create(this, "CognitoUserPool")
				.userPoolName(cognitoDomain)
				.signInAliases(SignInAliases.builder().email(true).build())
				.autoVerify(AutoVerifiedAttrs.builder().email(true).build())
				.signInCaseSensitive(false)
				.mfa(Mfa.OPTIONAL)
				.mfaSecondFactor(MfaSecondFactor.builder().otp(true).sms(false).build())
				.email(
						UserPoolEmail.withSES(
								UserPoolSESOptions.builder()
										.fromEmail(emailAddress)
										.fromName("JarHC")
										.build()
						)
				)
				.selfSignUpEnabled(true)
				.accountRecovery(AccountRecovery.EMAIL_ONLY)
				.passwordPolicy(
						PasswordPolicy.builder()
								.minLength(8)
								.requireUppercase(true)
								.requireLowercase(true)
								.requireDigits(true)
								.requireSymbols(true)
								.tempPasswordValidity(Duration.days(7))
								.build()
				)
				// TODO: .deviceTracking(DeviceTracking.builder().build())
				// TODO: .standardAttributes(StandardAttributes.builder()
				//		.email(StandardAttribute.builder().required(true).mutable(false).build())
				//		.build())
				.removalPolicy(RemovalPolicy.DESTROY) // TODO: this does not seem to have an effect - BUG?
				.build();

		// Cognito app client used by public website and API gateway
		UserPoolClientOptions cognitoAppClientOptions = UserPoolClientOptions.builder()
				.userPoolClientName(websiteDomain)
				.generateSecret(false)
				.oAuth(
						OAuthSettings.builder()
								.flows(
										OAuthFlows.builder()
												.authorizationCodeGrant(true)
												.implicitCodeGrant(true)
												.clientCredentials(false)
												.build()
								)
								.scopes(list(OAuthScope.OPENID, OAuthScope.PROFILE, OAuthScope.EMAIL, OAuthScope.PHONE))
								.defaultRedirectUri("https://" + websiteDomain + "/login.html")
								.callbackUrls(list("https://" + websiteDomain + "/login.html", "http://localhost:3000/login.html"))
								.logoutUrls(list("https://" + websiteDomain + "/logout.html", "http://localhost:3000/logout.html"))
								.build()
				)
				.idTokenValidity(Duration.hours(3))
				.accessTokenValidity(Duration.hours(3))
				.refreshTokenValidity(Duration.days(7))
				.enableTokenRevocation(true)
				.preventUserExistenceErrors(true)
				.supportedIdentityProviders(list(UserPoolClientIdentityProvider.COGNITO))
				// TODO: .readAttributes(list("name"))
				// TODO: .writeAttributes(list("name"))
				.build();
		UserPoolClient cognitoAppClient = cognitoUserPool.addClient(websiteDomain, cognitoAppClientOptions);

		// reference to SSL certificate for Cognito custom domain
		ICertificate cognitoCertificate = Certificate.fromCertificateArn(this, "CognitoCertificate", cognitoCertificateArn);

		// Cognito custom domain
		UserPoolDomainOptions cognitoCustomDomainOptions = UserPoolDomainOptions.builder()
				.customDomain(
						CustomDomainOptions.builder()
								.domainName(cognitoDomain)
								.certificate(cognitoCertificate)
								.build()
				)
				.build();
		UserPoolDomain cognitoCustomDomain = cognitoUserPool.addDomain("CognitoCustomDomain", cognitoCustomDomainOptions);

		// reference to Route 53 hosted zone for root domain
		IHostedZone hostedZone = HostedZone.fromLookup(this, "HostedZone", HostedZoneProviderProps.builder().domainName(rootDomain).build());

		// DNS record for Cognito custom domain
		ARecord.Builder.create(this, "CognitoDnsRecord")
				.zone(hostedZone)
				.recordName(cognitoDomain)
				.target(RecordTarget.fromAlias(new UserPoolDomainTarget(cognitoCustomDomain)))
				.build();

		createOutput(
				"CognitoClientID",
				cognitoAppClient.getUserPoolClientId(),
				"Cognito App Client ID"
		);
		createOutput(
				"CognitoDiscoveryEndpointURL",
				cognitoUserPool.getUserPoolProviderUrl() + "/.well-known/openid-configuration",
				"Cognito Discovery Endpoint URL"
		);
		createOutput(
				"CognitoIssuerURL",
				cognitoUserPool.getUserPoolProviderUrl(),
				"Cognito Issuer URL"
		);
		createOutput(
				"CognitoUserPoolARN",
				cognitoUserPool.getUserPoolArn(),
				"Cognito User Pool ARN"
		);

	}

}
