# Online Book Library & Reservation Platform (REST API)

[![Java 17](https://img.shields.io/badge/Java-17-ED8B00?style=flat&logo=openjdk&logoColor=white)](https://www.oracle.com/java/)
[![Spring Boot 3.0](https://img.shields.io/badge/Spring%20Boot-3.0.10-6DB33F?style=flat&logo=springboot&logoColor=white)](https://spring.io/projects/spring-boot)
[![Spring Security 6](https://img.shields.io/badge/Spring%20Security-6.0-6DB33F?style=flat&logo=springsecurity&logoColor=white)](https://spring.io/projects/spring-security)
[![JWT Authentication](https://img.shields.io/badge/Auth-Stateless%20JWT-000000?style=flat&logo=jsonwebtokens&logoColor=white)](https://jwt.io/)
[![Spring Data JPA](https://img.shields.io/badge/ORM-Hibernate%20%2F%20JPA-59666C?style=flat&logo=hibernate&logoColor=white)](https://spring.io/projects/spring-data-jpa)
[![Build Tool](https://img.shields.io/badge/Build-Gradle-02303A?style=flat&logo=gradle&logoColor=white)](https://gradle.org/)

A secure, enterprise-grade Book Management & Circulation RESTful API built with **Java 17**, **Spring Boot 3**, and **Spring Security 6**. The system enforces stateless **JWT (JSON Web Token)** authentication, granular Role-Based Access Control (RBAC), transactional book lending/reservation workflows, and custom centralized exception handling.

---

## 🏛️ Security & Architectural Flow

```
   [ Client Request ] ───► [ JwtAuthFilter ] ───► [ SecurityContextHolder ]
                                 │
                         Validates JWT Signature
                                 │
                                 ▼
                     [ Method Security: @PreAuthorize ]
                     ├── Has 'ROLE_ADMIN' ───► Book Catalog CRUD / User Audit
                     └── Has 'ROLE_CUSTOMER' ─► Borrow / Reserve / Review
                                 │
                                 ▼
                     [ Service Layer Transaction ]
                     ├── @Transactional State Transitions
                     ├── Reserve Queue Prioritization
                     └── History Audit Logging
                                 │
                                 ▼
                     [ Spring Data JPA / MySQL ]
```

---

## ✨ Core Engineering Features

1. **Spring Security 6 & Stateless JWT:**
   - Intercepts requests via `JwtAuthFilter` extending `OncePerRequestFilter`.
   - Validates HMAC-SHA256 signed tokens and populates `SecurityContextHolder`.
   - Role separation between administrative personnel (`ROLE_ADMIN`) and standard members (`ROLE_CUSTOMER`).
2. **Transactional Circulation Engine:**
   - Manages complete lifecycle states: `AVAILABLE`, `BORROWED`, `RESERVED`.
   - Prevents race conditions and double-borrowing with transactional boundary checks.
   - Preserves historical audit records in `UserHistory` for compliance and reading metrics.
3. **Centralized Global Exception Handling:**
   - `@ControllerAdvice` (`GlobalExceptionHandler`) translating custom domain exceptions (`BookNotAvailableException`, `NoReservationException`, `IdNotFoundException`) into structured HTTP 400/404 responses.
4. **Data Transfer Object (DTO) Pattern:**
   - Leverages `ModelMapper` for clean segregation between internal JPA database entities and public REST API representations.

---

## 📋 Comprehensive API Contract

### 1. Authentication & Identity
| Method | Endpoint | Description | Auth Required |
| :--- | :--- | :--- | :--- |
| `POST` | `/user/register` | Register new user account with assigned role | Public |
| `POST` | `/user/login` | Authenticate with credentials and receive JWT token | Public |

### 2. Catalog Management (Admin)
| Method | Endpoint | Description | Authorized Role |
| :--- | :--- | :--- | :--- |
| `POST` | `/books/create` | Add new book to library catalog | `ROLE_ADMIN` |
| `PUT` | `/books/update` | Modify book metadata or quantity | `ROLE_ADMIN` |
| `DELETE` | `/books/delete` | Soft-delete book from circulation | `ROLE_ADMIN` |
| `GET` | `/books/all` | List all books in the library | `ROLE_ADMIN`, `ROLE_CUSTOMER` |

### 3. Circulation Operations (Customer)
| Method | Endpoint | Description | Authorized Role |
| :--- | :--- | :--- | :--- |
| `POST` | `/books/{bookId}/borrow` | Borrow an available book copy | `ROLE_CUSTOMER` |
| `PUT` | `/books/{bookId}/return` | Return a borrowed book | `ROLE_CUSTOMER` |
| `POST` | `/books/{bookId}/reserve` | Place a reservation on a currently borrowed book | `ROLE_CUSTOMER` |
| `PUT` | `/books/{bookId}/cancel-reservation` | Cancel active reservation | `ROLE_CUSTOMER` |

### 4. Community Reviews
| Method | Endpoint | Description | Authorized Role |
| :--- | :--- | :--- | :--- |
| `POST` | `/books/{bookId}/reviews/create` | Submit rating & review for a book | `ROLE_CUSTOMER` |
| `GET` | `/books/{bookId}/reviews` | Fetch verified reviews for a title | `ROLE_ADMIN`, `ROLE_CUSTOMER` |
| `PUT` | `/books/{bookId}/reviews/{reviewId}/update` | Edit existing review | `ROLE_CUSTOMER` |

---

## 🚀 Quick Start Guide

### Prerequisites
- Java JDK 17
- Gradle 7+ (or use included `./gradlew`)
- MySQL 8.0+

### 1. Configure Database
In `src/main/resources/application.properties`:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/OnlineBookLibrary?createDatabaseIfNotExist=true&useSSL=false
spring.datasource.username=root
spring.datasource.password=your_password
spring.jpa.hibernate.ddl-auto=update
server.port=8080
```

### 2. Run the Application
```bash
./gradlew bootRun
```

### 3. Test Authentication via cURL

**Step 1: Authenticate to Obtain Token**
```bash
curl -X POST http://localhost:8080/user/login \
     -H "Content-Type: application/json" \
     -d '{
       "username": "customer_user",
       "password": "Password123!"
     }'
```
*Response:*
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
}
```

**Step 2: Access Protected Endpoint**
```bash
curl -X GET http://localhost:8080/books/all \
     -H "Authorization: Bearer <YOUR_JWT_TOKEN>"
```

---

## 👨‍💻 Author
**Avijit Paul**  
- **Role:** Full Stack Software Engineer  
- **LinkedIn:** [linkedin.com/in/avijitpaulavi](https://www.linkedin.com/in/avijitpaulavi/)  
- **GitHub:** [github.com/X-AVI-X](https://github.com/X-AVI-X)  
- **Email:** [avijit.paul.cs@gmail.com](mailto:avijit.paul.cs@gmail.com)