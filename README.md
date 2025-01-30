Authentication and Authorization Using JWT
This project demonstrates how to implement Authentication and Authorization in a Spring Boot application using JSON Web Tokens (JWT) for secure communication. It provides a basic example of how to manage user roles and permissions in a RESTful application.

Features
User Registration and Login
Role-Based Access Control (RBAC) with JWT
Protects routes based on user roles (e.g., Admin, User)
Implements JWT-based stateless authentication
Supports token-based authorization for API calls
Technology Stack
Spring Boot
Spring Security
JWT (JSON Web Tokens)
Maven
MySQL (or any other preferred database)
Prerequisites
Java 8 or higher
Maven
MySQL (or a preferred database)
IDE like IntelliJ IDEA, Eclipse, etc.
Getting Started
1. Clone the repository
To get started, clone the repository using Git:
```bash
git clone https://github.com/Aditya-96108/Authentication_and_Authorisation_Using_JWT.git
cd Authentication_and_Authorisation_Using_JWT
```
2. Setup the Database
Create a new MySQL database (or use your preferred database).
Update the database connection details in application.properties or application.yml.
Example for MySQL setup in src/main/resources/application.properties:
```bash
spring.datasource.url=jdbc:mysql://localhost:3306/auth_db
spring.datasource.username=root
spring.datasource.password=root
spring.jpa.hibernate.ddl-auto=update
```
3. Build the project
You can build the project using Maven:
```bash
mvn clean install
```
4. Run the application
To start the application, run the following:
```bash
mvn spring-boot:run
```
Once the application is running, you can access it via http://localhost:8080.
Endpoints
1. Register User
POST /api/auth/register

Registers a new user.

Request Body:
```bash
{
  "username": "user123",
  "password": "password123",
  "role": "USER"
}
```
2. Login and Get JWT Token
POST /api/auth/login

Logs in the user and returns a JWT token.

Request Body:
```bash
{
  "username": "user123",
  "password": "password123"
}
```
