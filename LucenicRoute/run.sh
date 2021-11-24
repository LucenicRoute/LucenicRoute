#!/bin/bash

# Compile project
mvn clean package

# Run queries using given k1 and b values.
CUSTOM_K1=1.2
CUSTOM_B=0.75
if [ "$1" == "-ci" ]; then
java -jar target/LucenicRoute-1.0-SNAPSHOT.jar -ci -k1 $CUSTOM_K1 -b $CUSTOM_B
else
java -jar target/LucenicRoute-1.0-SNAPSHOT.jar -k1 $CUSTOM_K1 -b $CUSTOM_B
fi

# Run trec_eval
trec_eval -m map qrels-assignment2.part1 results
