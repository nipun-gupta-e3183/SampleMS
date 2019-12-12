
Setting up development environment
==================================
* Install & start [Kafka](https://kafka.apache.org/quickstart)
* Run Kafka processor

        ../mvnw spring-boot:run
        
* Add a todo by posting it in Kafka

        $ echo '{"title": "via kafka", "completed": false}' | ./bin/kafka-console-producer.sh --broker-list localhost:9092 --topic todos

Useful commands
===============
* Build with tests

        ../mvnw clean build
