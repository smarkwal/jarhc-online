FROM golang:1.17-alpine AS builder

WORKDIR /go/src
COPY ./ ./

RUN CGO_ENABLED=0 go build -o ../bin

RUN wget -O /go/bin/jarhc.jar https://github.com/smarkwal/jarhc/releases/download/v2.0.0/jarhc-2.0.0-with-deps.jar

FROM eclipse-temurin:11.0.14.1_1-jre-alpine

COPY --from=builder /go/bin/ /var/task/

WORKDIR /var/task

ENTRYPOINT ["/var/task/jarhc-check"]
