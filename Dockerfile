FROM openjdk:21
ARG JAR_FILE=target/bank_app_backend.jar
COPY ${JAR_FILE} bank_app_backend.jar
ENTRYPOINT ["java","-jar","/bank_app_backend.jar"]