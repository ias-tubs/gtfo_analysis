#!/bin/bash
start_server()
{
  until joern -J-Xmx16G -J-Xss128m --server --server-port $1 >&$1.log; do
        echo "Joern server on port $1 crashed with code $?. Respawning..."
        sleep 1
    done

}

cd ..

start_server 8080 &
start_server 8081 &
start_server 8082 &
start_server 8083 &
start_server 8084 &
start_server 8085 &
start_server 8086 &
start_server 8087 &

trap 'echo "quit"; kill $(jobs -p); exit' SIGINT EXIT

while true
do
    sleep 1
done
