FROM azul/zulu-openjdk-alpine:17-jre

WORKDIR /app

COPY --from=target ./*.jar app.jar

COPY entrypoint.sh entrypoint.sh

RUN chmod 755 entrypoint.sh

EXPOSE 8001

ENTRYPOINT ./entrypoint.sh