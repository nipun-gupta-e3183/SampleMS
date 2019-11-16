
Setting up development environment
==================================
* Install OpenJDK 11
* Create `todo` schema in MySQL

        CREATE DATABASE todo CHARACTER SET utf8mb4
    
* Run DB Migration

        ../mvnw -Dflyway.configFiles=src/main/resources/flyway.conf flyway:migrate -pl api/
        
* Run api server

        ../mvnw spring-boot:run -pl api/
        
* Generate JWT token from https://jwt.io using following information
    * Secret: secret1 (for "service1" client as per application.properties)
    * Algorithm: HS256
    * Payload

            {
              "ClientId": "service1",
              "UserId": "123",
              "AccId": "1",
              "OrgId": "2",
              "Permissions": "31",
              "iat": 1516239022
            }

* Add a todo

        curl -X POST \
          http://localhost:8080/api/v1/todos \
          -H 'Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJDbGllbnRJZCI6InNlcnZpY2UxIiwiVXNlcklkIjoiMTIzIiwiQWNjSWQiOiIxIiwiT3JnSWQiOiIyIiwiUGVybWlzc2lvbnMiOiIzMSIsImlhdCI6MTUxNjIzOTAyMn0._8riHr0rVqZmdcTmiI8bEm6lgi3tTjuGLN4OnySIq0c' \
          -H 'Content-Type: application/json' \
          -d '{
        	"title": "Buy milk"
        }'

* Fetch all todos

        curl -X GET \
          http://localhost:8080/api/v1/todos \
          -H 'Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJDbGllbnRJZCI6InNlcnZpY2UxIiwiVXNlcklkIjoiMTIzIiwiQWNjSWQiOiIxIiwiT3JnSWQiOiIyIiwiUGVybWlzc2lvbnMiOiIzMSIsImlhdCI6MTUxNjIzOTAyMn0._8riHr0rVqZmdcTmiI8bEm6lgi3tTjuGLN4OnySIq0c' \
          -H 'Content-Type: application/json'
