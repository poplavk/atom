FROM openjdk:8-jre-alpine
MAINTAINER Dmitry Bragin
COPY build/libs/homeworks/HW3-1.0-SNAPSHOT.jar /services/game_server.jar
#EXPOSE will not open port to outer world, just inside cloud
EXPOSE 8089
CMD ["java", "-jar", "/services/game_server.jar"]