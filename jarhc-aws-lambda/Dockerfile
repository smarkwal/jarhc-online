FROM eclipse-temurin:11-jdk-alpine AS build-stage

# unzip function
WORKDIR /function
ADD ./build/distributions/jarhc-aws-lambda-1.0.0.zip ./function.zip
RUN unzip function.zip && rm function.zip

#-------------------------------------------------------------------------------------------
FROM eclipse-temurin:11-jdk-alpine

# install perl
RUN apk update && \
    apk upgrade && \
    apk add perl

# copy JAPICC
COPY --from=smarkwal/japicc:2.4 /usr/bin/japi-compliance-checker /usr/bin/japi-compliance-checker
COPY --from=smarkwal/japicc:2.4 /usr/share/japi-compliance-checker /usr/share/japi-compliance-checker

# copy function
WORKDIR /function
COPY --from=build-stage /function /function

# configure runtime startup
ENTRYPOINT [ "/opt/java/openjdk/bin/java", "-cp", "./lib/*", "com.amazonaws.services.lambda.runtime.api.client.AWSLambda" ]

# pass name of function handler as argument to runtime
CMD [ "org.jarhc.online.japicc.Handler::handleRequest" ]
