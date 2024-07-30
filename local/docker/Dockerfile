FROM azul/zulu-openjdk-alpine:17-jre

WORKDIR /app

ADD https://search.maven.org/remotecontent?filepath=co/elastic/apm/elastic-apm-agent/1.37.0/elastic-apm-agent-1.37.0.jar /app/elastic-apm-agent.jar

COPY --from=target ./*.jar app.jar

COPY entrypoint.sh entrypoint.sh

RUN chmod 755 entrypoint.sh

EXPOSE 8001

ENTRYPOINT ./entrypoint.sh