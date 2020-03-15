#!/bin/bash

echo "replacing secrets from travis secure variables"
sed -i "" -e "s|%MONGO_PWD%|MONGO_PWD|g" src/main/resources/application.properties

actual_value=MONGO_PWD

echo $actual_value