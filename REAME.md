📌 Task Management REST API

A RESTful backend application built with Spring Boot that provides user management functionality following a layered architecture design pattern.

This project demonstrates core backend development concepts including REST API design, data persistence with JPA, exception handling, and clean architecture separation.

🚀 Tech Stack:
Java 17+
Spring Boot
Spring Data JPA
Hibernate
MySQL / H2 DB
Maven
Postman (API testing)

🏗️ Architecture

This project follows a Layered Architecture pattern:

Controller → Service → Repository → Database
Responsibilities:
Controller – Handle HTTP requests & responses
Service – Business logic layer
Repository – Data access layer (Spring Data JPA)
Entity – Database mapping
DTO – Data transfer objects
Exception – Global & custom exception handling

This separation ensures maintainability, scalability, and clean code structure.


⚙️ Features

Create User
Retrieve All Users
Retrieve User by ID
Update User
Delete User
Global Exception Handling
Custom Exceptions (ResourceNotFoundException, DuplicateResourceException)
RESTful API Design Principles

🛠️ How to Run

1️⃣ Clone project

git clone https://github.com/tannhat0602/JavaMiniProject
cd JavaMiniProject

2️⃣ Run application

mvn spring-boot:run

or

mvn clean package
java -jar target/*.jar

Application runs at:

http://localhost:8080


🌐 API Endpoints
Method	    Endpoint	    Description
GET	        /users	        Get all users
GET	        /users/{id}	    Get user by id
POST	    /users	        Create new user
PUT	        /users/{id}	    Update user
DELETE	    /users/{id}	    Delete user

📸 API Testing

Tested using Postman.

Example JSON body:

{
  "username": "Tan",
  "email": "tan@example.com"
}


🧠 Key Learning Outcomes

Through this project, I practiced:

Designing RESTful APIs
Implementing layered architecture
Handling exceptions globally in Spring Boot
Using Spring Data JPA for database operations
Managing Maven project structure
Testing APIs using Postman

🔥 Future Improvements

Add Request Validation (Bean Validation)
Implement Pagination & Sorting
Add Authentication & Authorization (JWT)
Integrate Swagger/OpenAPI documentation
Add Unit & Integration Tests
Dockerize the application


👨‍💻 Author

Phung Nhat Tan
Aspiring Backend Developer
Focused on Java & Spring Boot Development