#!/bin/zsh
mvn clean install -P jar -DskipTests
cp ./gamer/target/gamer.jar ~/countdown-jars/
mv ~/countdown-jars/gamer.jar ~/countdown-jars/adam-gamer.jar
