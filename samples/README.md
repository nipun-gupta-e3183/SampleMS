
Setting up development environment
==================================
* Install OpenJDK 11
* Configure our corporate maven repository by creating `~/.m2/settings.xml` with following content

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
          <activeProfiles>
            <!--make the profile active all the time -->
            <activeProfile>nexus</activeProfile>
          </activeProfiles>
        </settings>

* Create `todo` schema in MySQL

        CREATE DATABASE todo CHARACTER SET utf8mb4
    
* Run DB Migration

        ../mvnw flyway:migrate -pl db/
        
* For running API server, please refer [API readme](./api/README.md).
* For running SQS message processor, please refer [SQS Processor readme](./sqs-processor/README.md).