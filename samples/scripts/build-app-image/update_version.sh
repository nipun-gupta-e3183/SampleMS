#!/bin/sh
cp mvnsettings.xml /root/.m2/settings.xml && ./mvnw versions:set-property -Dproperty=revision -DnewVersion=$1