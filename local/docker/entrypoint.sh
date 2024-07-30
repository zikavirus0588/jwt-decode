#!/bin/sh

. /etc/profile

java -jar -Djava.security.egd=file:/dev/./urandom ./app.jar