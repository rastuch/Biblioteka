
# Biblioteka

Biblioteka is a student project of library management system implemented in Java using Spring Boot. This application allows managing books, copies, users, loans, fines, and reservations in a library.

## Table of Contents
- [Features](#features)
- [Requirements](#requirements)
- [Installation](#installation)
- [Configuration](#configuration)
- [Running the Application](#running-the-application)
- [Testing](#testing)
- [API Endpoints](#api-endpoints)
- [Contributing](#contributing)
- [License](#license)

## Features
- User management
- Book and copy management
- Loan and return books
- Fine management
- Reservation management
- RESTful API with Swagger documentation

## Requirements
- Java 17
- Maven 3.6+
- PostgreSQL (or another supported database)

## Installation

1. **Clone the repository:**
   ```bash
   git clone https://github.com/rastuch/Biblioteka.git
   cd Biblioteka
   ```

2. **Set up the database:**
   Create a PostgreSQL database and update the configuration in \`src/main/resources/application.properties\`.

   ```properties
   spring.datasource.url=jdbc:postgresql://localhost:5432/yourdatabase
   spring.datasource.username=yourusername
   spring.datasource.password=yourpassword
   ```

3. **Build the project:**
   ```bash
   mvn clean install
   ```

## Configuration

- **application.properties:**
  Configure your database settings and other application-related properties.

  ```properties
    spring.application.name=Biblioteka
    server.port=8086
    logging.level.org.springframework.security=DEBUG
    jwt.expiration.days=360
    springdoc.swagger-ui.operationsSorter=method
    springdoc.pathsToMatch=/api/**
    spring.datasource.url=jdbc:postgresql://localhost:5432/yourdatabase
    spring.datasource.username=yourusername
    spring.datasource.password=yourpassword
  ```

## Running the Application

1. **Run the Spring Boot application:**
   ```bash
   mvn spring-boot:run
   ```

2. **Access the application:**
   Open your browser and go to http://localhost:8086 .

## Testing

- **Tests Library:**
  The application includes JUnit 4 tests. 
## API Endpoints

The API documentation is available via Swagger. Once the application is running, access it at http://localhost:8086/swagger-ui.html .

### Examples

- **User API**
  - `GET /api/users\`: Get all users
  - `POST /api/users\`: Add a new user
  - `PUT /api/users\`: Update a user
  - `DELETE /api/users\`: Delete a user by ID

- **Book API**
  - `GET /api/books\`: Get all books
  - `POST /api/books\`: Add a new book
  - `PUT /api/books\`: Update a book
  - `DELETE /api/books\`: Delete a book by ID

- **Copy API**
  - `GET /api/copy\`: Get copy by ID
  - `POST /api/copy\`: Add a new copy
  - `PUT /api/copy\`: Update a copy
  - `DELETE /api/copy\`: Delete a copy by ID

- **Loan API**
  - `GET /api/loan\`: Get loan by ID
  - `POST /api/loan\`: Add a new loan
  - `PUT /api/loan\`: Update a loan
  - `DELETE /api/loan\`: Delete a loan by ID

- **Fine API**
  - `GET /api/fine\`: Get fine by ID
  - `POST /api/fine\`: Add a new fine
  - `PUT /api/fine\`: Update a fine
  - `DELETE /api/fine\`: Delete a fine by ID

- **Reservation API**
  - `GET /api/reservation\`: Get reservation by ID
  - `POST /api/reservation\`: Add a new reservation
  - `PUT /api/reservation\`: Update a reservation
  - `DELETE /api/reservation\`: Delete a reservation by ID

## Contributing

Contributions are welcome! Please create a pull request or open an issue to discuss your changes.

## License

This project is licensed under the MIT License.
