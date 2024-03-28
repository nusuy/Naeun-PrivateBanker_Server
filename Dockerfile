FROM openjdk:17-jdk
LABEL authors="yusunchoi"
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-Dspirng.profiles.active=prod","-jar","/app.jar"]