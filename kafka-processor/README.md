
Setting up development environment
==================================
* Install & start [Kafka](https://kafka.apache.org/quickstart)
* Run Kafka processor

        ../mvnw spring-boot:run
        
* Add a todo by posting it in Kafka

        $ echo 'todo#todo_create#1#1234#1.0.0${ "data": { "account_id": 1, "organisation_id": 2, "service": "freshdesk", "payload": { "account_full_domain": "tmk.freshdesk.com", "model_properties": { "title": "via kafka", "completed": false } } } }' | ./bin/kafka-console-producer.sh --broker-list localhost:9092 --topic todos --property 'parse.key=true' --property 'key.separator=$'

Useful commands
===============
* Build with tests

        ../mvnw clean build
