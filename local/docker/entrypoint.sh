#!/bin/sh

. /etc/profile

java -javaagent:/app/elastic-apm-agent.jar -jar -Djava.security.egd=file:/dev/./urandom ./app.jar