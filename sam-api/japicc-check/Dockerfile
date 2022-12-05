FROM eclipse-temurin:11-jdk-alpine

# install perl
RUN apk update && \
    apk upgrade && \
    apk add perl

# copy JAPICC
COPY --from=smarkwal/japicc:2.4 /usr/bin/japi-compliance-checker /usr/bin/japi-compliance-checker
COPY --from=smarkwal/japicc:2.4 /usr/share/japi-compliance-checker /usr/share/japi-compliance-checker

# add function
WORKDIR /function
ADD ./target/dependency/*.jar ./
ADD ./target/japicc-check-1.0.0-SNAPSHOT.jar ./

# configure runtime startup
ENTRYPOINT [ "/opt/java/openjdk/bin/java", "-cp", "./*", "com.amazonaws.services.lambda.runtime.api.client.AWSLambda" ]

# pass name of function handler as argument to runtime
CMD [ "org.jarhc.online.japicc.Handler::handleRequest" ]
