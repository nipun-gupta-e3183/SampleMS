FROM maven:3-jdk-11 as builder
ENV BUILD_ROOT /build
ENV JAR_FILE api-0.0.1-SNAPSHOT.jar
ADD . $BUILD_ROOT
WORKDIR $BUILD_ROOT
RUN mvn -DskipTests install

FROM openjdk:11-jdk
ENV APP_ROOT /api
ENV JAR_FILE api-0.0.1-SNAPSHOT.jar
WORKDIR $APP_ROOT
COPY --from=builder $BUILD_ROOT/api/target/$JAR_FILE $APP_ROOT
ENTRYPOINT [ "sh", "-c", "java -jar $JAR_FILE" ]
EXPOSE 8080