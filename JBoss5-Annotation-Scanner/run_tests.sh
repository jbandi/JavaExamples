#!/bin/bash
mvn clean package -DskipTests
rm -rf testdata
mkdir testdata
cp -R ear/target testdata
mvn test