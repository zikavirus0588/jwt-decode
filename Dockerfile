FROM maven:3.8.5-openjdk-17-slim AS build

WORKDIR /app

COPY pom.xml .
COPY src ./src

RUN mvn clean package -e -DskipTests

FROM azul/zulu-openjdk-alpine:17-jre

WORKDIR /app

COPY --from=build /app/target/*.jar app.jar

COPY entrypoint.sh entrypoint.sh

RUN chmod 755 entrypoint.sh

EXPOSE 8001

ENTRYPOINT ./entrypoint.sh