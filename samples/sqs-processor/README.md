
Setting up development environment
==================================
* Install docker & [LocalStack](https://github.com/localstack/localstack)
* Start local SQS service using LocalStack

        SERVICES=sqs PORT_WEB_UI=9090 ./bin/localstack start

* Create `todos` SQS queue

        $ aws --endpoint-url=http://127.0.0.1:4576/ sqs create-queue --queue-name todos

* Run SQS processor

        ../mvnw spring-boot:run
        
* Add a todo by posting it in SQS

        $ aws --endpoint-url=http://127.0.0.1:4576/ sqs send-message --queue-url http://localhost:4567/queue/todos --message-body '{"product":"fd", "account_id": "2", title": "Inserted via SQS"}'

Useful commands
===============
* Build with tests

        ../mvnw clean build
