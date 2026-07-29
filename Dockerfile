FROM maven:3.8.8-eclipse-temurin-8 AS build

ENV MYSQL_CONNECT_URL=jdbc:mysql://db:3306/
ENV MYSQL_TEST_DB_NAME=demo_test
ENV MYSQL_DB_NAME=demo

WORKDIR /app

COPY pom.xml .

COPY src ./src


CMD mvn jetty:run -Dmysql.url=jdbc:mysql://db:3306