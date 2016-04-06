# Spring Cloud Event Sourcing Example

This reference application is a Spring Cloud example of using event sourcing in microservices. The project is intended to demonstrate end-to-end best practices for building a _Netflix-like_ microservice architecture using Spring Cloud.

* Spring Cloud OAuth2
  * Authorization Server
  * Resource Server
* Edge Service
  * API gateway with OAuth2 protected resources
  * OAuth2 SSO
* Event-driven Messaging
  * Event sourcing

## Architecture Diagram

![Online Store Architecture Diagram](http://i.imgur.com/PBCVt90.png)

## Online Store Domain

This reference application is based on common design patterns for building an ecommerce application. The application includes the following microservices.

* Discovery Service
* Edge Service
* User Service
* Catalog Service
* Account Service
* Order Service
* Inventory Service
* Online Store Web
* Shopping Cart Service

## Usage

Microservice architectures commonly use multiple databases. The resources of the business domain are distributed across the microservice architecture, with each service having its own exclusive database. Each type of database for a microservice is commonly chosen by a development team based on its advantages when solving a specific problem.

This reference application uses the following mixture of databases.

* MySQL - RDBMS
* Neo4j - GraphDB
* MongoDB - Document Store
* Redis - Key/value Store

### Integration Testing

If you would like to use Docker for integration testing, a `docker-compose.yml` file has been provided in the root directory. To build all the images, first make sure that you have Docker installed and available in your command line tool. With Docker and Docker Compose installed, execute the provided `run.sh` script. This script will build each container and start each of the services and database dependencies. When all the services have started up. Verify that the services are registered with Eureka at `http://$DOCKER_IP:8761`.

If everything has loaded correctly, navigate to the online store at `http://DOCKER_IP:8787/`. Click `Login`. You'll be navigated to the authorization server's gateway at `http://DOCKER_IP:8181/uaa/login`. The username is `user` and the password is `password`. You'll be authenticated and asked to approve token grant to the online web store. After accepting the grant, you'll be redirected to the online store application where you'll be able to access protected resources from the edge service.

## License

This project is licensed under Apache License 2.0.
