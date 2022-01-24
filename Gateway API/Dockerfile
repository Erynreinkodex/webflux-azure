FROM adoptopenjdk/openjdk11:alpine-jre

WORKDIR /opt/app
ARG JAR_FILE=target/gateway-api-1.0-SNAPSHOT.jar
COPY ${JAR_FILE} ./
ENTRYPOINT ["java","-jar","gateway-api-1.0-SNAPSHOT.jar"]