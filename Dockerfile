FROM maven:3.9.6-amazoncorretto-21 AS build
WORKDIR /app
COPY pom.xml .
COPY src/ /app/src/
RUN mvn package -DskipTests

FROM amazoncorretto:21-alpine3.19-jdk
WORKDIR /app
COPY --from=build /app/pom.xml /app/pom.xml
COPY --from=build /app/src/main/resources/application.yaml /app/application.yaml
COPY --from=build /app/target/*.jar /app/ticket.jar

ENTRYPOINT ["java", "-jar", "ticket.jar"]
CMD ["--spring.config.location=file:application.yaml"]
