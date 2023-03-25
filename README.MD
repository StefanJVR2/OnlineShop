# README.md

## Overview

This README contains the necessary information to understand, set up, and run two Spring Boot applications: External Product System and Online Shop API. These applications use RabbitMQ to synchronize data between them. Both applications use H2 as a database and are compiled with Gradle. The applications have been developed using Java 19.

The External Product System is responsible for sending product price updates to the Online Shop API. The Online Shop API receives the updates via RabbitMQ and updates its own H2 database.

## Prerequisites

- Java 19
- Docker
- Docker Compose
- Gradle 7.0 or higher

## Directory Structure
```
Online Shop System
├── external-product-system
├── online-shop-api
└── docker-compose.yml
```

## Setting up the Applications

1. Clone the repository to your local machine:

```bash
git clone https://github.com/StefanJVR2/OnlineShop.git
cd online-shop-system
```
2. Build both applications using Gradle:
```   cd external-product-system
./gradlew clean build
cd ../online-shop-api
./gradlew clean build
cd ..
```
## Running the Applications with Docker Compose
Make sure Docker and Docker Compose are installed on your machine.

In the root directory of the project, where the docker-compose.yml file is located, run the following command to start both applications and RabbitMQ:

```docker-compose up --build```

This command will build the Docker images for both applications, start the containers, and set up RabbitMQ.

To stop and remove the containers and associated resources, run:

```docker-compose down```

## Accessing the Applications
External Product System: Runs in the background and sends product price updates to the queue.
* Online Shop API: http://localhost:8080
Remember to replace `localhost` with the appropriate IP address if accessing the applications from a different machine.

## API Documentation
Please refer to the API documentation for the Online Shop API to understand the available endpoints and their usage:

### Online Shop API: http://localhost:8080/swagger-ui.html
### Online Shop API Endpoints

- `GET /products`: Returns a list of all products available in the Online Shop API.
    
    #### Example:
    
    Request:
    ```http
    GET /products HTTP/1.1
    Host: localhost:8080
    ```
    Response:
    ```json
    [
      {
        "id": 1,
        "name": "Product 1",
        "description": "Product 1 description",
        "price": 9.99
      },
      {
        "id": 2,
        "name": "Product 2",
        "description": "Product 2 description",
        "price": 19.99
      }
    ]
    ```
* `GET /products/{id}`: Retrieves a specific product by its ID from the Online Shop API.
    #### Example:
    
    Request:
    ```http request
    GET /products/1 HTTP/1.1
    Host: localhost:8080
    ```
    
    Response:
    ```json
    {
      "id": 1,
      "name": "Product 1",
      "description": "Product 1 description",
      "price": 9.99
    }
    
    ```
* `GET /products`: Retrieves products given a search term from the Online Shop API.
  #### Example:

  Request:
    ```http request
    GET /products?searchTerm=some%20Descrption HTTP/1.1
    Host: localhost:8080
    ```

  Response:
    ```json
    [{
      "id": 1,
      "name": "Product 1",
      "description": "Some",
      "price": 9.99
    },
    {
      "id": 2,
      "name": "Product 1",
      "description": "Description",
      "price": 10.99
    }]
    
    ```
