# Sample Products Service

## Overview
This is a sample multi module maven project which conatins a set of Restful API operations on Product. The project caters a variety of CRUD operations on product/product options.

## Project Structure

This is a multi module maven project. This project is compiled in Java 8 with [Spring Boot](https://spring.io/projects/spring-boot) following [microservice pattern](https://spring.io/microservices). The Project has three modules:

- The `micoservice` module produces a spring boot application.
- The `functional-tests` is used to run functional tests using the [karate](https://github.com/intuit/karate) library.
- The `docker` module is used to package the microservice artefact into a docker image and push it onto a docker registry.

## Compile & Test
Basic Prerequisite :
1. Maven Setup
2. Open JDK 8 or Oracle Java 8

Running in local:
* Once you have the above Prerequisites, compile the project code and run the below command. This would run the entire build for all modules and execute tests with creating artefacts.

  `mvn clean install`

* You could also run only the application by using below command :
  
  `mvn spring-boot:run`

The project also uses [JaCoCo](https://www.eclemma.org/jacoco/) which is a free code coverage library for Java. 
Once the tests are executed(using `mvn clean install`) the code coverage report is generated in
below locations.

* Jacoco data file location - `functional-tests/target/jacoco-merged.exec` 

* Jacoco Coverage Report location - `functional-tests/target/jacoco-report/index.html`


## Code Style
The Project uses [Google Java Code Style](https://github.com/google/styleguide/blob/gh-pages/intellij-java-google-style.xml).

## Swagger docs location

*  http://localhost:8080/v2/api-docs
*  http://localhost:8080/swagger-ui.html
*  The API documentation makes use of Springfox OpenAPI swagger libraries.

## API Enpoints
1. `GET /products` - Gets all products.
2. `GET /products?name={name}` - Finds all products matching the specified name.
3. `GET /products/{id}` - Gets the project that matches the specified ID - ID is a UUID.
4. `POST /products` - Creates a new product.
5. `PUT /products/{id}` - Updates a product.
6. `DELETE /products/{id}` - Deletes a product and its options.
7. `GET /products/{id}/options` - Finds all options for a specified product.
8. `GET /products/{id}/options/{optionId}` - Finds the specified product option for the specified product - optionId is a UUID.
9. `POST /products/{id}/options` - Adds a new product option to the specified product.
10. `PUT /products/{id}/options/{optionId}` - Updates the specified product option.
11. `DELETE /products/{id}/options/{optionId}` - Deletes the specified product option.

## Docker Build
To create a docker image from this artefact add the pom profile `build-docker-image` and a docker image will get created in your registry. Command as follows :

  `mvn clean install -P build-docker-image`
  
## Coverage Report
![Merged Coverage Report](coverage_report.png)  