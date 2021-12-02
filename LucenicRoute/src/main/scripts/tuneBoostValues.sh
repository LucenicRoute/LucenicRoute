#!/bin/bash

# Navigate to directory containing pom.xml file
cd ../../..

# Compile project
mvn clean package

# Prepare results file
echo -n "" > mapResultsForBoosts.csv
printf "Title boost value,Content boost value,Date boost value,Publication boost value,map score\n" >> mapResultsForBoosts.csv

# Iterate through boost values, writing values and map result to file
PATTERN='[0-9]+\.[0-9]+'
for PUBLICATION_BOOST in $(seq 0 1 5)
do
for DATE_BOOST in $(seq 0 1 5)
do
for CONTENT_BOOST in $(seq 0 1 5)
do
for TITLE_BOOST in $(seq 0 1 5)
do
java -jar target/LucenicRoute-1.0-SNAPSHOT.jar -boosts $TITLE_BOOST,$CONTENT_BOOST,$DATE_BOOST,$PUBLICATION_BOOST
RESULT=$(trec_eval -m map qrels-assignment2.part1 results)
[[ $RESULT =~ $PATTERN ]]
MAP_RESULT=${BASH_REMATCH[0]}
printf "$TITLE_BOOST,$CONTENT_BOOST,$DATE_BOOST,$PUBLICATION_BOOST,$MAP_RESULT\n" >> mapResultsForBoosts.csv
done
done
done
done
