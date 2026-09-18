# REST API — Clean Architecture with Spring Boot

![Java](https://img.shields.io/badge/Java-21-orange?logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-4.1.1-brightgreen?logo=springboot&logoColor=white)
![REST](https://img.shields.io/badge/API-REST-red?logo=fastapi&logoColor=white)
![Maven](https://img.shields.io/badge/Maven-Build-blue?logo=apachemaven&logoColor=white)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-16-blue?logo=postgresql&logoColor=white)
![Flyway](https://img.shields.io/badge/Flyway-Migrations-red?logo=flyway&logoColor=white)

> **REST API** implemented following the principles of **Clean Architecture**, using **Spring Boot 4.1.1**, with persistence in **PostgreSQL** (via Docker) and migrations managed by **Flyway**, organized into domain, application and infrastructure layers.

> The entire project (code, comments and documentation) is written in English.

---

## Motivation

This project was created to demonstrate the practical application of Clean Architecture in a simple domain, keeping the focus on software engineering practices rather than the complexity of business rules.

The goal is to explore, in practice, how separation of responsibilities, dependency rules, testing strategies, persistence, error handling, and framework integration can coexist in a Spring Boot application.

More than just making the application work, the project aims to make the decisions behind its structure explicit, keeping them testable and easy to evolve. The structure can also serve as a reference or starting point for other projects, allowing different domains to be developed based on the same architectural principles.


---

## 📦 Technologies

| Technology          | Version               |
| ------------------- | --------------------- |
| **Java**            | 21                    |
| **Spring Boot**     | 4.1.1                 |
| **PostgreSQL**      | 16 (Docker)           |
| **Spring Web**      | Spring Boot BOM       |
| **Spring Data JPA** | Spring Boot BOM       |
| **Flyway**          | Spring Boot BOM       |
| **Lombok**          | Spring Boot BOM       |
| **Testcontainers**  | Spring Boot BOM       |

---

## 🧱 Project Structure

The project follows the principles of **Clean Architecture**, separating responsibilities into layers:

```text
com.afralves.cleanarchitecture
├── domain
│   ├── entity
│   └── exception
├── application
│   ├── usecases
│   │   ├── createuser
│   │   ├── deleteuser
│   │   ├── listusers
│   │   └── updateuserpassword
│   ├── gateway
│   └── exceptions
├── infrastructure
│   └── adapter
│       ├── controller
│       │   ├── request
│       │   └── response
│       ├── persistence
│       │   ├── repository
│       │   ├── model
│       │   └── converter
│       └── exception
└── main               (beans / app configuration)
```

Each layer has its own README with objective, dependency rule, what lives there, what does **not** live there, and the project's architectural decisions (with the "why"):

- [`domain/`](src/main/java/com/afralves/cleanarchitecture/domain/README.md) — entities and business invariants. Pure core.
- [`application/`](src/main/java/com/afralves/cleanarchitecture/application/README.md) — use cases, gateways (interfaces) and flow exceptions.
- [`infrastructure/`](src/main/java/com/afralves/cleanarchitecture/infrastructure/README.md) — HTTP, JPA persistence and error mapping adapters.
- [`main/`](src/main/java/com/afralves/cleanarchitecture/main/README.md) — composition root: where the layers meet.

---

## 🚀 How to run

**1. Clone the repository:**

```bash
git clone https://github.com/your-user/cleanarchitecture.git
cd cleanarchitecture
```

**2. Start the PostgreSQL database (Docker):**

```bash
make db-up
```

> The container does **not** start automatically with Docker (`restart: "no"`). Start it manually whenever you develop and shut it down with `make db-down` when you finish.

| Command         | Action                                  |
| --------------- | --------------------------------------- |
| `make db-up`    | Starts Postgres in the background       |
| `make db-down`  | Shuts down the container                |
| `make db-logs`  | Follows the Postgres logs               |
| `make db-status`| Shows the container status              |

| Config       | Value        |
| ------------ | ------------ |
| **Host**     | `localhost`  |
| **Port**     | `55432`      |
| **Database** | `cleanarch`  |
| **User**     | `cleanarch`  |
| **Password** | `cleanarch`  |

**3. Build and run the project:**

```bash
./mvnw spring-boot:run
```

Flyway automatically applies the migrations from `src/main/resources/db/migration/` on startup.

**4. Access the application:**

| Resource       | URL                                       |
| -------------- | ----------------------------------------- |
| 🌐 **API**     | `http://localhost:8888`                   |
| 📖 **Swagger** | `http://localhost:8888/swagger-ui.html`   |
| 📄 **OpenAPI** | `http://localhost:8888/v3/api-docs`       |

---

## 💻 API usage examples with `curl`

<details>
<summary><strong>🔸 Create user</strong></summary>

```bash
curl -X POST http://localhost:8888/rest/v1/users \
  -H "Content-Type: application/json" \
  -d '{
    "email": "example@test.com",
    "password": "123456",
    "name": "John Doe"
  }'
```

</details>

<details>
<summary><strong>🔸 Update user password</strong></summary>

```bash
curl -X PUT http://localhost:8888/rest/v1/users \
  -H "Content-Type: application/json" \
  -d '{
    "email": "example@test.com",
    "password": "newPassword123"
  }'
```

</details>

<details>
<summary><strong>🔸 Delete user</strong></summary>

```bash
curl -X DELETE http://localhost:8888/rest/v1/users/youremail@test.com
```

</details>

<details>
<summary><strong>🔸 List all users</strong></summary>

```bash
curl -X GET http://localhost:8888/rest/v1/users
```

</details>

---

## 🧪 Tests

The project separates **unit tests**, **architecture tests** and **integration tests** by naming convention:

| Type          | Suffix       | Maven tool            | Requires Docker      |
| ------------- | ------------ | --------------------- | -------------------- |
| Unit          | `*Test.java` | Surefire              | No                   |
| Architecture  | `*Test.java` | Surefire (ArchUnit)   | No                   |
| Integration   | `*IT.java`   | Failsafe              | Yes (Testcontainers) |

The architecture tests use **ArchUnit** to validate Clean Architecture rules at build time: dependencies between layers, class location and naming, isolation of the domain from frameworks, and absence of cycles between packages. They live in `com.afralves.cleanarchitecture.architecture` (`CleanArchitectureTest`, `PackageStructureTest`, `NamingConventionTest`).

| Command         | Action                                                              |
| --------------- | ------------------------------------------------------------------- |
| `make test`     | Unit + architecture tests (fast, no Docker)                         |
| `make test-it`  | Unit + architecture + integration (`mvn verify`, starts container)  |

---

### 👨‍💻 Created by

Developed by [@afralves](https://github.com/afralves)
