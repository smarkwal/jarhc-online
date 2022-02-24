FROM golang:1.17-alpine AS builder

# copy Go source code to /src
WORKDIR /src
COPY src /src

# build Go application
RUN CGO_ENABLED=0 go build

#-------------------------------------------------------------------------------
FROM alpine

# prepare working directory /app
WORKDIR /app

# copy Go application to /app
COPY --from=builder --chmod=755 /src/rest-api /app/rest-api

# create user jarhc without password and login shell
# and set user jarhc as owner of working directory /app
RUN adduser -D -s /sbin/nologin jarhc jarhc && \
    chown -R jarhc.jarhc /app

# run Go application as user jarhc
USER jarhc

# expose HTTP port 8080
EXPOSE 8080/tcp

# run Go application
CMD ["/app/rest-api"]
