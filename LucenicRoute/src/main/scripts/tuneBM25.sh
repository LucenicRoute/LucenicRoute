#!/bin/bash

# Navigate to directory containing pom.xml file
cd ../../..

# Compile project
mvn clean package

# Prepare results file
echo -n "" > mapResults.csv
printf "K1,b,map\n" >> mapResults.csv

# Iterate through k1 and b values, writing values and map result to file
PATTERN='[0-9]+\.[0-9]+'
for CUSTOM_K1 in $(seq 0 0.1 3)
do
for CUSTOM_B in $(seq 0 0.1 1)
do
java -jar target/LucenicRoute-1.0-SNAPSHOT.jar -k1 $CUSTOM_K1 -b $CUSTOM_B
RESULT=$(trec_eval -m map qrels-assignment2.part1 results)
[[ $RESULT =~ $PATTERN ]]
MAP_RESULT=${BASH_REMATCH[0]}
printf "$CUSTOM_K1,$CUSTOM_B,$MAP_RESULT\n" >> mapResults.csv
done
done
