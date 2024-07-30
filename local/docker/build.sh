#!/bin/sh

fail() {
  echo $2
  exit $1
}

printf 'Packing application.. \n'
printf '\n'

cd ../../

if mvn clean install -DskipTests -q; then
  printf 'Success: application packed successfully. \n'
else
  fail 1 "Failed: unable to pack application"
fi

printf '\n'
printf 'Build jwt-decode container with docker compose.. \n'
printf '\n'

cd ./local/docker || exit

if docker compose up -d --build; then
  printf '\n'
  printf 'Success: jwt-decode container created successfully.'
else
  fail 1 "Failed: unable to create jwt-container"
fi

printf '\n'

cd ../../

printf 'You may now test your application \n'
