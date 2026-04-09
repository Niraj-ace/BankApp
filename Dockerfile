FROM eclipse-temurin:8-jdk

WORKDIR /app

COPY target/bankapp.jar app.jar

EXPOSE 8081

ENTRYPOINT ["java","-Duser.timezone=UTC","-jar","app.jar"]