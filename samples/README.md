
Setting up development environment
==================================
* Install OpenJDK 11
* Create `todo` schema in MySQL

        CREATE DATABASE todo CHARACTER SET utf8mb4
    
* Run DB Migration

        ../mvnw flyway:migrate -pl db/
        
* For running API server, please refer [API readme](./api/README.md).
* For running SQS message processor, please refer [SQS Processor readme](./sqs-processor/README.md).