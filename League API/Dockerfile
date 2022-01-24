FROM adoptopenjdk/openjdk11:alpine-jre

WORKDIR /opt/app
ARG JAR_FILE=target/league-api-1.0-SNAPSHOT.jar
COPY ${JAR_FILE} ./
ENTRYPOINT ["java","-jar","league-api-1.0-SNAPSHOT.jar"]