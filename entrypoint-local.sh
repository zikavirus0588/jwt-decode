#!/bin/sh

. /etc/profile

java -javaagent:/app/elastic-apm-agent.jar -Delastic.apm.application_packages=br.com.gomesar.* -jar -Djava.security.egd=file:/dev/./urandom ./app.jar