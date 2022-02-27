FROM golang:1.17-alpine AS builder

WORKDIR /go/src
COPY ./ ./

RUN CGO_ENABLED=0 go build -o ../bin

FROM smarkwal/japicc:2.4

COPY --from=builder /go/bin/ /var/task/

WORKDIR /var/task

ENTRYPOINT ["/var/task/japicc-check"]
