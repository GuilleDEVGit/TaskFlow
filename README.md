# Taskflow Backend

API REST desarrollada con **Spring Boot** para la gestión de usuarios y sus tareas.

Este proyecto sigue una arquitectura limpia por capas y está enfocado en aplicar **buenas prácticas reales de backend**, incluyendo testing, validación y diseño estructurado de APIs.

---

## 🚀 Funcionalidades

- Creación, actualización y eliminación de usuarios
- Creación de tareas asociadas a usuarios
- Obtención de tareas filtradas por usuario
- Uso de DTOs para manejo de requests/responses
- Validación de datos con Bean Validation
- Manejo global de excepciones
- Diseño de API RESTful
- Arquitectura en capas (Controller → Service → Repository)

---

## 🧪 Testing

El proyecto incluye distintos tipos de tests cubriendo varias capas:

- **Tests de controladores** con Spring MVC (`@WebMvcTest`)
  - Validación de peticiones HTTP
  - Testing de seguridad con roles (ADMIN)
  - Protección CSRF

- **Tests de repositorio** con JPA (`@DataJpaTest`)
  - Interacción con base de datos real usando H2 (en memoria)
  - Validación de queries (ej: buscar tareas por userId)
  - Aislamiento de datos con rollback transaccional

- **Tests de servicios** (en progreso / planificados)
  - Validación de lógica de negocio con Mockito

---

## 🛠️ Tecnologías

- Java 17+
- Spring Boot
- Spring Data JPA (Hibernate)
- H2 (testing)
- MySQL / PostgreSQL (producción)
- Maven
- Lombok
- Bean Validation
- JUnit 5 + Mockito
- Spring Security (control de acceso por roles)
- Swagger (documentación y testing de API)

---

## 📂 Estructura del proyecto

- **Controller**: Endpoints REST (capa HTTP)
- **Service**: Lógica de negocio
- **Repository**: Acceso a datos (JPA)
- **DTOs**: Contratos de entrada/salida
- **Exception**: Manejo global de errores
- **Test**: Tests unitarios e integración

---

## 🔐 Seguridad

- Control de acceso basado en roles
- Protección de endpoints mediante `@PreAuthorize`
- Ejemplo: solo usuarios con rol ADMIN pueden modificar o eliminar usuarios

---

## 🧠 Conceptos aplicados

- Separación de responsabilidades (arquitectura por capas)
- Uso de DTOs para desacoplar la API de las entidades
- Testing con base de datos real en memoria (H2)
- Uso de Mockito para pruebas aisladas
- Diseño RESTful y uso correcto de códigos HTTP

---