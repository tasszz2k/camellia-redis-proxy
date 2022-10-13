# syntax = docker/dockerfile:experimental
# First stage: complete build environment
FROM maven:3.5-jdk-8-alpine AS builder
WORKDIR /app
# add pom.xml and source code
ADD pom.xml pom.xml
ADD src src/

# package jar
#RUN --mount=type=cache,target=/root/.m2 mvn clean package -DskipTests -Dhttp.proxyHost=10.50.69.2 -Dhttp.proxyPort=3128 -Dhttps.proxyHost=10.50.69.2 -Dhttps.proxyPort=3128

# Second stage: minimal runtime environment
FROM openjdk:8-jre-alpine
WORKDIR /app

ENV http_proxy=http://10.50.69.2:3128
ENV https_proxy=http://10.50.69.2:3128
RUN apk update && apk add --no-cache libc6-compat gcompat
# copy jar from the first stage
COPY --from=builder /app/target/camellia-redis-proxy-samples-*.jar camellia-redis-proxy-1.0-SNAPSHOT.jar
EXPOSE 6380

CMD ["java", "-jar", "-Dspring.config.location=/app/","camellia-redis-proxy-1.0-SNAPSHOT.jar"]