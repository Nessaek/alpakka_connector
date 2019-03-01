#!/usr/bin/env bash

readonly kafka_dir=~/kafka/2.1.0

cd ${kafka_dir}

bin/kafka-server-stop config/server.properties