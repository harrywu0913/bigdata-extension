#!/usr/bin/env bash

source=$1
brokers=$2
topic=$3

cat ${source} | python sleep.py | kafka-console-producer --broker-list ${brokers} --topic ${topic}