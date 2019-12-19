FROM maven:3-jdk-11 as builder
ENV BUILD_ROOT /build
COPY ./docker/mvnsettings.xml /root/.m2/settings.xml
WORKDIR $BUILD_ROOT
COPY ./pom.xml $BUILD_ROOT/
COPY ./db/pom.xml $BUILD_ROOT/db/
COPY ./common/pom.xml $BUILD_ROOT/common/
COPY ./api/pom.xml $BUILD_ROOT/api/
COPY ./kafka-processor/pom.xml $BUILD_ROOT/kafka-processor/
COPY ./sqs-processor/pom.xml $BUILD_ROOT/sqs-processor/
RUN mvn -B clean install -DskipTests -Dcheckstyle.skip -Dasciidoctor.skip -Djacoco.skip -Dmaven.gitcommitid.skip -Dspring-boot.repackage.skip -Dmaven.exec.skip=true -Dmaven.install.skip -Dmaven.resources.skip  -Dcodegen.skip --fail-never
COPY . $BUILD_ROOT
RUN mvn -DskipTests install

FROM openjdk:11-jdk
ENV APP_ROOT /api
ENV BUILD_ROOT /build
WORKDIR $APP_ROOT
COPY --from=builder $BUILD_ROOT/api/target/api-*.jar $APP_ROOT/
COPY --from=builder $BUILD_ROOT/kafka-processor/target/kafka-processor-*.jar $APP_ROOT/
COPY --from=builder $BUILD_ROOT/sqs-processor/target/sqs-processor-*.jar $APP_ROOT/