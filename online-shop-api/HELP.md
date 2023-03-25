# Online Shop API
## Getting Started

This is a Gradle-based Spring Boot application for an online shop API. The application includes an `onReceive()` method that listens for messages on a RabbitMQ queue, updates the price of a product, and saves the updated product to a database.

To run the application, you can use the Gradle `bootRun` command:

```./gradlew bootRun```

This will start the application on port 8080.

## `onReceive()` Method

The onReceive() method is a Spring Consumer that listens for messages on a RabbitMQ queue. When a message is received, the method reads the message as a JSON string and deserializes it into a Product object.

The method then checks if the Product already exists in the database by calling the existsById() method on the ProductRepository. If the Product does not exist in the database, the insertProduct flag on the Product is set to true.

The method then updates the price of the Product to a random value between 0 and 100, saves the Product to the database using the save() method on the ProductRepository, and logs the updated Product information.

If any exceptions occur during the processing of the message, an OnlineShopException is thrown.
## `/products` Endpoint

The application exposes a `/products` endpoint that retrieves all products from the database. You can call this endpoint using `curl`:

```curl http://localhost:8080/products```

### Reference Documentation

For further reference, please consider the following sections:

* [Official Gradle documentation](https://docs.gradle.org)
* [Spring Boot Gradle Plugin Reference Guide](https://docs.spring.io/spring-boot/docs/3.0.4/gradle-plugin/reference/html/)
* [Create an OCI image](https://docs.spring.io/spring-boot/docs/3.0.4/gradle-plugin/reference/html/#build-image)
* [Spring Data JPA](https://docs.spring.io/spring-boot/docs/3.0.4/reference/htmlsingle/#data.sql.jpa-and-spring-data)
* [Spring Boot DevTools](https://docs.spring.io/spring-boot/docs/3.0.4/reference/htmlsingle/#using.devtools)
* [Spring Reactive Web](https://docs.spring.io/spring-boot/docs/3.0.4/reference/htmlsingle/#web.reactive)
* [Liquibase Migration](https://docs.spring.io/spring-boot/docs/3.0.4/reference/htmlsingle/#howto.data-initialization.migration-tool.liquibase)
* [Spring for RabbitMQ](https://docs.spring.io/spring-boot/docs/3.0.4/reference/htmlsingle/#messaging.amqp)
* [Cloud Stream](https://docs.spring.io/spring-cloud-stream/docs/current/reference/html/spring-cloud-stream.html#spring-cloud-stream-overview-introducing)

### Guides

The following guides illustrate how to use some features concretely:

* [Accessing Data with JPA](https://spring.io/guides/gs/accessing-data-jpa/)
* [Building a Reactive RESTful Web Service](https://spring.io/guides/gs/reactive-rest-service/)
* [Messaging with RabbitMQ](https://spring.io/guides/gs/messaging-rabbitmq/)

### Additional Links

These additional references should also help you:

* [Gradle Build Scans – insights for your project's build](https://scans.gradle.com#gradle)

