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

# To resolve dependencies in a safe way (no re-download when the source code changes)
RUN mvn clean package -Dmaven.main.skip -Dmaven.test.skip && rm -r target
# To package the application
RUN mvn clean package -Dmaven.test.skip
#RUN --mount=type=cache,target=/root/.m2 mvn clean package  -Dmaven.test.skip

FROM openjdk:8-jre-alpine

# This is the directory where the output of CMD should run
WORKDIR $APP_HOME

COPY --from=builder $JAR_FILE ./camellia-dashboard.jar
EXPOSE $PORT

CMD ["java", "-jar", "./camellia-dashboard.jar"]