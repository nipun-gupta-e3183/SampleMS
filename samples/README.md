
Setting up development environment
==================================
* Install OpenJDK 11
* Create `todo` schema in MySQL

        CREATE DATABASE todo CHARACTER SET utf8mb4
    
* Run DB Migration

        ../mvnw -Dflyway.configFiles=src/main/resources/flyway.conf flyway:migrate -pl api/
        
* Run api server

        ../mvnw spring-boot:run -pl api/
        
* Generate JWT token from https://jwt.io using following information (sample https://jwt.io/#debugger-io?token=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJVc2VySWQiOiIxMjMiLCJBY2NJZCI6IjEiLCJPcmdJZCI6IjIiLCJQZXJtaXNzaW9ucyI6IjMxIiwiaWF0IjoxNTE2MjM5MDIyfQ.9O8FQyoBW69VIQseQdKX-RVosaqEVKQ6lZvB1IjffAk)
    * Secret: secret1 (for "service1" client as per application.properties)
    * Algorithm: HS256
    * Payload

            {
              "UserId": "123",
              "AccId": "1",
              "OrgId": "2",
              "Permissions": "31",
              "iat": 1516239022
            }

* Add a todo

        curl -X POST \
          http://localhost:8080/api/v1/todos \
          -H 'Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJVc2VySWQiOiIxMjMiLCJBY2NJZCI6IjEiLCJPcmdJZCI6IjIiLCJQZXJtaXNzaW9ucyI6IjMxIiwiaWF0IjoxNTE2MjM5MDIyfQ.9O8FQyoBW69VIQseQdKX-RVosaqEVKQ6lZvB1IjffAk' \
          -H 'Content-Type: application/json' \
          -H 'X-Client-ID: service1' \
          -d '{
        	"title": "Buy milk"
        }'

* Fetch all todos

        curl -X GET \
          http://localhost:8080/api/v1/todos \
          -H 'Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJVc2VySWQiOiIxMjMiLCJBY2NJZCI6IjEiLCJPcmdJZCI6IjIiLCJQZXJtaXNzaW9ucyI6IjMxIiwiaWF0IjoxNTE2MjM5MDIyfQ.9O8FQyoBW69VIQseQdKX-RVosaqEVKQ6lZvB1IjffAk' \
          -H 'X-Client-ID: service1' \
          -H 'Content-Type: application/json'

Useful commands
===============
* Build with tests

        spring_profiles_active=dev ../mvnw clean build
