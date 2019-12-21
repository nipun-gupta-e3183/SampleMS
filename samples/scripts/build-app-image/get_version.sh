#!/bin/sh
cp mvnsettings.xml /root/.m2/settings.xml && ./mvnw help:evaluate -Dchangelist= -Dexpression=project.version -q -DforceStdout