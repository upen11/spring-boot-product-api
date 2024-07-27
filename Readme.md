# Spring Boot Product Management API

This project is a Spring Boot application for managing products. It includes CRUD operations for products and integrates various technologies and best practices for modern Java development.

## Features

- CRUD operations for products
- Integration with MySQL database
- Logging with SLF4J and Logback
- Exception handling with @RestControllerAdvice
- Unit and integration tests
- Basic security setup (future implementation)
- Potential extensions with Kafka, Redis, Microservices, Docker, Kubernetes, etc.

## Getting Started

### Prerequisites

- Java 17 or higher
- Maven 3.6.3 or higher
- MySQL database

### Installation

1. Clone the repository
    ```sh
    git clone https://github.com/your-username/spring-boot-product-api.git
    cd spring-boot-product-api
    ```

2. Update application properties
   Edit the `src/main/resources/application.properties` file to configure your MySQL database settings.
    ```properties
    spring.datasource.url=jdbc:mysql://localhost:3306/your-database
    spring.datasource.username=your-username
    spring.datasource.password=your-password
    spring.jpa.hibernate.ddl-auto=update
    ```

3. Build the project
    ```sh
    ./mvnw clean install
    ```

4. Run the application
    ```sh
    ./mvnw spring-boot:run
    ```

## Running Tests

The project includes unit and integration tests. To run the tests, use the following command:
```sh
./mvnw test
```

## API Endpoints
### Products
````
GET /api/v1/products - Get all products
GET /api/v1/products/{id} - Get product by ID
GET /api/v1/products/name/{name} - Get products by name
GET /api/v1/products/category/{category} - Get products by category
GET /api/v1/products/price/asc - Get products sorted by price ascending
GET /api/v1/products/price/desc - Get products sorted by price descending
POST /api/v1/products - Create a new product
PUT /api/v1/products/{id} - Update an existing product
DELETE /api/v1/products/{id} - Delete a product by ID
````

## Logging
Logging is configured using SLF4J and Logback. Logs are written to both the console and a file located at logs/app.log.

## Exception Handling
Global exception handling is implemented using @RestControllerAdvice and @ExceptionHandler annotations.

## Future Work
- **Security:** Implement authentication and authorization using Spring Security and JWT.
- **Caching:** Integrate Redis for caching.
- **Messaging:** Integrate Kafka for message-driven architecture.
- **Microservices:** Refactor the application into microservices.
- **Containerization**: Dockerize the application.
- **Orchestration**: Deploy using Kubernetes and Helm.
- **CI/CD:** Set up continuous integration and deployment pipelines.
- **Cloud:** Deploy the application on AWS using services like Lambda, ECS, etc.