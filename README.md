# Getting Started

# Recipe API
Recipe API allows users to manage their favorite recipes. Allows adding, updating, removing and fetching recipes.


## Prerequisites
- Java 11
- <a href="https://spring.io/projects/spring-boot">Spring Boot</a> - Framework to ease the bootstrapping and development of new Spring Applications
- <a href="https://maven.apache.org/">Maven</a> - Dependency Management

## Technologies
- <a href="https://www.liquibase.org/">Liquibase</a> - Open source relational database migration tool and rapidly track, version, and deploy database schema changes.
- <a href="https://www.postgresql.org/about/">PostgresSQL</a> - Open source object-relational database system


## Installation and Usage

### PostgreSQL Setup
#### Using a Local installation

- Start Databases:

  ```shell
  pg_ctl -D <Path for PostgreSQL 'Data' dir> -l logfile start
  ```

- Stop Databases:
  ```shell
  pg_ctl -D <Path for PostgreSQL 'Data' dir> -l logfile stop
  ```

### Maven
- Build project using Maven

  ```shell
  mvn clean package
  ```

- Running the Application in Java

  ```shell
  java -jar target/abnamro-recipe-api-<version>.jar
  ```

- Running the application with Maven
    ```shell
    mvn spring-boot:run
    ```

### API Endpoints
Local API - http://localhost:8080

### Documentation

* [Swagger Page](http://127.0.0.1:8080/swagger-ui/index.html)

## Postman Script
Environment: * [pst/ABN-AMRO.postman_environment.json ]({{PROJECT_ROOT}}/pst/ABN-AMRO.postman_environment.json)

Collection: * [pst/abnamro-recipeAPI-v1.postman_collection.json ]({{PROJECT_ROOT}}/pst/abnamro-recipeAPI-v1.postman_collection.json)

## PlantUML
Sequential UML: * [plantuml/recipe-sequence-diagram.puml ](plantuml/recipe-sequence-diagram.puml)

Sequential IMAGE: * [plantuml/ABN_AMRO_Recipe_API-Recipe_API__create_recipe_scenario.png ](plantuml/ABN_AMRO_Recipe_API-Recipe_API__create_recipe_scenario.png)

## Unit & Integration test
Test result: * [root/pst/Test Results - All_in_abnamro-recipe-api.html ] ({{PROJECT_ROOT}}/pst/Test Results - All_in_abnamro-recipe-api.html)

### Reference Documentation

For further reference, please consider the following sections:

* [Official Apache Maven documentation](https://maven.apache.org/guides/index.html)
* [Spring Boot Maven Plugin Reference Guide](https://docs.spring.io/spring-boot/docs/2.7.3/maven-plugin/reference/html/)
* [Create an OCI image](https://docs.spring.io/spring-boot/docs/2.7.3/maven-plugin/reference/html/#build-image)
* [Spring Web](https://docs.spring.io/spring-boot/docs/2.7.3/reference/htmlsingle/#web)
* [Spring Reactive Web](https://docs.spring.io/spring-boot/docs/2.7.3/reference/htmlsingle/#web.reactive)
* [Spring Boot Actuator](https://docs.spring.io/spring-boot/docs/2.7.3/reference/htmlsingle/#actuator)
* [Liquibase Migration](https://docs.spring.io/spring-boot/docs/2.7.3/reference/htmlsingle/#howto.data-initialization.migration-tool.liquibase)
* [Spring Data JPA](https://docs.spring.io/spring-boot/docs/2.7.3/reference/htmlsingle/#data.sql.jpa-and-spring-data)

### Guides

The following guides illustrate how to use some features concretely:

* [Building a RESTful Web Service](https://spring.io/guides/gs/rest-service/)
* [Serving Web Content with Spring MVC](https://spring.io/guides/gs/serving-web-content/)
* [Building REST services with Spring](https://spring.io/guides/tutorials/rest/)
* [Building a Reactive RESTful Web Service](https://spring.io/guides/gs/reactive-rest-service/)
* [Building a RESTful Web Service with Spring Boot Actuator](https://spring.io/guides/gs/actuator-service/)
* [Accessing Data with JPA](https://spring.io/guides/gs/accessing-data-jpa/)

