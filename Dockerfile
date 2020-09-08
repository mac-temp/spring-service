FROM adoptopenjdk/openjdk11:jre-11.0.8_10-alpine

WORKDIR /opt/application

COPY target/*.jar ./application.jar

ENTRYPOINT ["java","-jar","application.jar"]
