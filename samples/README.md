# Freshworks Boot samples

This repo contains sample projects for building different types of microservices using
[freshworks-boot](https://github.com/freshdesk/freshworks-boot/) starters/libraries.

Steps for starting a new micro-service development
==============
1. Create a new Git repo for your project

        git clone <your_git_repo>
        git remote add boot-samples git@github.com:freshdesk/freshworks-boot-samples.git
        git fetch boot-samples --tags
        git merge --allow-unrelated-histories boot-samples/master
        git push
    
1. Copy `./samples/*` into the root.

        cp -R samples/* .
    
1. Start developing your modules based on the files in the root. Please don't delete `samples` directory as that will
allow us to find new changes done in the `freshworks-boot-samples` project by doing git diff.

Setting up development environment
==================================
* Install OpenJDK 11
* Configure our corporate maven repository by creating `~/.m2/settings.xml` with following content. Replace SYSTEM_USERNAME and SYSTEM_PASSWORD with your system username and password

        <settings>
          <mirrors>
            <mirror>
              <!--This sends everything else to /public -->
              <id>nexus</id>
              <mirrorOf>*</mirrorOf>
              <url>https://nexuscentral.runwayci.com/repository/maven-public/</url>
            </mirror>
          </mirrors>
          <profiles>
            <profile>
              <id>nexus</id>
              <!--Enable snapshots for the built in central repo to direct -->
              <!--all requests to nexus via the mirror -->
              <repositories>
                <repository>
                  <id>central</id>
                  <url>http://central</url>
                  <releases><enabled>true</enabled></releases>
                  <snapshots><enabled>true</enabled></snapshots>
                </repository>
              </repositories>
             <pluginRepositories>
                <pluginRepository>
                  <id>central</id>
                  <url>http://central</url>
                  <releases><enabled>true</enabled></releases>
                  <snapshots><enabled>true</enabled></snapshots>
                </pluginRepository>
              </pluginRepositories>
            </profile>
          </profiles>
          <servers>
            <server>
                <id>nexus</id>
                <username>SYSTEM_USERNAME</username>
                <password>SYSTEM_PASSWORD</password>
            </server>
          </servers>
          <activeProfiles>
            <!--make the profile active all the time -->
            <activeProfile>nexus</activeProfile>
          </activeProfiles>
        </settings>
        
* Create `todo` schema in MySQL

        CREATE DATABASE todo CHARACTER SET utf8mb4
    
* Run DB Migration

        ../mvnw flyway:migrate -pl db/
        
* For running an API server, please refer [API readme](./api/README.md).
* For running an SQS message processor, please refer [SQS Processor readme](./sqs-processor/README.md).
* For running a Kafka message processor, please refer [Kafka Processor readme](./kafka-processor/README.md).
