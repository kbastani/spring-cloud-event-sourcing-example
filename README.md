# Spring Cloud Event Sourcing Example

Spring Cloud example of event sourcing using microservices.

* Spring Cloud OAuth2
  * Authorization Server
  * Resource Server
* Edge Service
  * API gateway with OAuth2 protected resources
  * OAuth2 SSO
* Event-driven Messaging
  * Event sourcing

## Usage

Some applications require database dependencies described in the application.yml files of each service.

* MySQL
* Neo4j
* MongoDB
* Redis

If using Docker for integration testing, you can use these commands to start the database dependencies.

    docker run -d -p 7474:7474 -e NEO4J_AUTH=none --name neo4j neo4j:latest
    docker run -d -p 27017:27017 --name mongo mongo:latest
    docker run --rm --env MYSQL_ROOT_PASSWORD=dbpass --env MYSQL_DATABASE=dev -p 3306:3306 --name mysql mysql:latest
    docker run -d -p 6379:6379 --name redis redis:latest

After starting the required database dependencies, run each service using `mvn spring-boot:run` in the following order.

* Discovery Service
* Edge Service
* User Service
* Catalog Service
* Account Service
* Order Service
* Inventory Service
* Online Store Web

When all the services have started up. Verify that the services are registered with Eureka at `http://localhost:8761`.

If everything has loaded correctly, navigate to the online store at `http://localhost:8787/`. Click `Login`. You'll be navigated to the authorization server's gateway at `http://localhost:8181/uaa/login`. The username is `user` and the password is `password`. You'll be authenticated and asked to approve token grant to the online web store. After accepting the grant, you'll be redirected to the online store application where you'll be able to access protected resources from the edge service.

## TODO

Test data must be inserted into each of the databases before you can see any data.
