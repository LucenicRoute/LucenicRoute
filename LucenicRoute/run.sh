#!/bin/bash

# Compile project
mvn clean package

if [ "$1" == "-ci" ]; then
java -jar target/LucenicRoute-1.0-SNAPSHOT.jar -ci
else
java -jar target/LucenicRoute-1.0-SNAPSHOT.jar
fi

# Run trec_eval
trec_eval -m map qrels-assignment2.part1 results
