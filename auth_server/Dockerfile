FROM openjdk:8-jre-alpine
MAINTAINER Dmitry Bragin
COPY build/libs/auth_server-1.0-SNAPSHOT.jar /services/auth_server.jar
#EXPOSE will not open port to outer world, just inside cloud
#Billing service port
EXPOSE 8080
CMD ["java", "-jar", "/services/auth_server.jar"]