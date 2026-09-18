# `infrastructure` layer

## Objective

`infrastructure` is the layer responsible for the **adapters** and for the application's communication with external resources, such as HTTP and the database.

Here live the implementations that use external frameworks and technologies, such as Spring MVC and Spring Data JPA. These components make the connection between the external interfaces and the contracts defined by the inner layers, such as `InputBoundary` and `Gateway`.

## Dependency rule

`infrastructure` may depend on `application` and `domain` to use their contracts and objects. It also concentrates the dependencies on external frameworks and technologies, such as Spring MVC, Spring Data JPA and Jackson.

The `application` and `domain` layers **do not depend on `infrastructure`**. When an external implementation is required, `infrastructure` implements the contract defined by the inner layer and `main` is responsible for connecting the two.

## What lives here

### `adapter/controller/`

Responsible for the HTTP input, including controllers and request/response DTOs.

### `adapter/persistence/`

Responsible for the data access and for the implementation of the gateways defined in `application`.

### `adapter/exception/`

Responsible for handling exceptions and converting application errors into HTTP responses.

## What does **not** live here

* **Business rules**: live in the inner layers, mainly in `domain` and in the flows coordinated by `application`.
* **Abstract contracts**: interfaces such as `UserGateway` live in `application/`. In `infrastructure/` lives their implementation, such as `UserRepositoryAdapter`.
* **Domain entities annotated with JPA**: `User` remains persistence-independent in `domain/`, while `UserEntity` represents the persisted data in `infrastructure/`. The conversion between the two is done by the converter.

## Project decisions

### JPA entity is separated from the domain entity

`User` (domain) and `UserEntity` (JPA) are different classes, and `UserEntityConverter` handles the conversion between them.

**Why:** annotations such as `@Entity`, `@Column` and `@Id` are persistence details and are not part of the domain. Keeping the classes separate prevents `domain/` from depending on JPA and keeps `User` independent of the way its data is persisted.

### Request/response records carry their own conversion

The same decision adopted in `application/` is used here:

* [`CreateUserRequest.toCreateUserInput()`](adapter/controller/request/CreateUserRequest.java): converts the HTTP request to the use case `Input`.
* [`CreatedUserResponse.from(output)`](adapter/controller/response/CreatedUserResponse.java) and [`ListUserResponse.from(outputs)`](adapter/controller/response/ListUserResponse.java): build the HTTP response from the use case `Output`.

Since these are simple conversions specific to these records, we do not use additional classes such as `RequestConverter` or `ResponseConverter`.

The same approach is explained in [`../application/README.md`](../application/README.md#boundary-records-carry-their-own-conversion).

### Conversion between domain and persistence

Unlike the request/response and input/output records, which carry their own conversion in the `.from(...)` / `.toX()` format, the JPA ↔ domain boundary uses a separate class. In this case, the conversion lives outside the types themselves.

The conversion logic between `User` (domain) and `UserEntity` (JPA) lives in its own class, injected into `UserRepositoryAdapter` via the constructor.

* **Single responsibility:** the adapter is responsible for orchestrating the persistence operations, while the converter exclusively handles the conversion between `User` and `UserEntity`.
* **Avoids duplication:** the conversion logic is centralized in a single place and reused by operations such as `saveUser`, `findUsers` and `findByEmail`.
* **Testability:** the conversion can be tested in isolation, without depending on JPA, repository or database.
* **`UserEntity` is a JPA entity:** it is a mutable class annotated by the framework (`@Entity`, `@Column`). Placing the conversion logic inside it would add another responsibility to the entity; the external converter keeps `UserEntity` focused on the persistence representation.

### Request/Response records are per endpoint

Each endpoint has its own request and response records, instead of sharing a `UserDto` across different operations.

**Why:** each endpoint has its own contract and may require different data. `password`, for example, makes sense in `CreateUserRequest`, but not in `CreatedUserResponse`. Keeping DTOs separate avoids unnecessary or optional fields and allows each operation to evolve independently.

### Centralized exception handling

The `GlobalExceptionHandler` centralizes the conversion of known exceptions from `domain` and `application` into HTTP responses. Each exception type is associated with the corresponding status, keeping this handling out of the controllers.

Unmapped exceptions are handled by a fallback with `@ExceptionHandler(Exception.class)`, which logs the error and returns a standardized `500` response without exposing internal details to the client.

## References

* Robert C. Martin, *Clean Architecture*, chapter 22, "The Clean Architecture".
