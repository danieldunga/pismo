FROM openjdk:11
MAINTAINER Daniel Braga <daniel.braga@yahoo.com.br>

VOLUME /tmp
COPY target/pismo*.jar app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]
