# The image we are creating is based on a Java 8 OpenJDK image.
FROM maven:3.5-jdk-8-alpine AS builder

# This sets the environmental variable.
ENV TZ=Asia/Ho_Chi_Minh
ENV PORT=6380
ENV APP_HOME=/app
ARG JAR_FILE=target/*.jar

# This set timezone
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

COPY ./pom.xml ./pom.xml
COPY ./src ./src

RUN mvn clean install

FROM openjdk:8-jre-alpine

# This is the directory where the output of CMD should run
WORKDIR $APP_HOME

COPY --from=builder $JAR_FILE ./camellia-redis-proxy.jar
EXPOSE $PORT

CMD ["java", "-jar", "./camellia-redis-proxy.jar"]