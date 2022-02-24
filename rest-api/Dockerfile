FROM golang:1.17-alpine AS builder

# copy Go source code to /src
WORKDIR /src
COPY src /src

# build Go application
RUN CGO_ENABLED=0 go build

#-------------------------------------------------------------------------------
FROM  eclipse-temurin:11-jdk

# install git and make
RUN apt-get update && \
    apt-get install git make -y

# download, build, and install japicc to /usr/bin/japi-compliance-checker
RUN git clone --depth 1 --branch 2.4 https://github.com/lvc/japi-compliance-checker.git /opt/japi-compliance-checker
RUN cd /opt/japi-compliance-checker && \
    make install prefix=/usr

# prepare working directory /app
WORKDIR /app

# copy Go application to /app
COPY --from=builder --chmod=755 /src/rest-api /app/rest-api

# create user jarhc without password and login shell
# and set user jarhc as owner of working directory /app
RUN groupadd jarhc && \
    useradd --create-home --gid jarhc --shell /usr/sbin/nologin --no-log-init jarhc && \
    chown -R jarhc.jarhc /app

# run Go application as user jarhc
USER jarhc

# expose HTTP port 8080
EXPOSE 8080/tcp

# run Go application
CMD ["/app/rest-api"]
