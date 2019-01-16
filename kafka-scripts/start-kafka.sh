#!/bin/bash

readonly kafka_dir=~/kafka_2.11-2.0.0

cd ${kafka_dir}

bin/kafka-server-start.sh config/server.properties

