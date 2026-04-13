# Taskflow Backend

Backend REST API built with **Spring Boot** to manage users and their tasks.

This project follows a clean, layered architecture and focuses on **real backend practices**, including testing, validation, and structured API design.

---

## 🚀 Features

* User creation, update, and deletion
* Task creation associated with users
* Retrieve tasks filtered by user
* DTO-based request/response handling
* Input validation with Bean Validation
* Global exception handling
* RESTful API design
* Layered architecture (Controller → Service → Repository)

---

## 🧪 Testing

The project includes different types of tests covering multiple layers:

* **Controller tests** using Spring MVC (`@WebMvcTest`)

    * HTTP request/response validation
    * Security testing with roles (ADMIN)
    * CSRF protection

* **Repository tests** using JPA (`@DataJpaTest`)

    * Real database interaction using H2 (in-memory)
    * Query validation (e.g. find tasks by userId)
    * Data isolation with transactional rollback

* **Service layer testing** (in progress / planned)

    * Business logic validation with Mockito

---

## 🛠️ Tech Stack

* Java 17+
* Spring Boot
* Spring Data JPA (Hibernate)
* H2 (testing)
* MySQL (production)
* Maven
* Lombok
* Bean Validation
* JUnit 5 + Mockito
* Spring Security (basic role-based access)
* Swagger (API testing)

---

## 📂 Project Structure

* **Controller**: REST endpoints (HTTP layer)
* **Service**: Business logic
* **Repository**: Database access (JPA)
* **DTOs**: Request/response contracts
* **Exception**: Global error handling
* **Test**: Unit and integration tests

---

## 🔐 Security

* Role-based access control
* Protected endpoints using `@PreAuthorize`
* Example: only ADMIN users can update or delete users

---

## 🧠 Key Concepts Applied

* Separation of concerns (layered architecture)
* DTO pattern to decouple API from entities
* Real database testing with in-memory DB (H2)
* Mocking with Mockito for isolated tests
* RESTful conventions and proper HTTP status usage

---

## 🚧 Future Improvements

* Refactor to entity relationships (`User` ↔ `Task`) instead of primitive IDs
* Introduce pagination and filtering
* Add integration tests (`@SpringBootTest`)
* Dockerize the application
* Explore microservices architecture (Spring Cloud, Kafka)

---
