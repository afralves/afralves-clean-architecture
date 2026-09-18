# `domain` layer

## Objective

`domain` is the **core of the application**. This is where the **entities and business rules** that must be preserved regardless of external details live.

In this project, `User` represents the user and concentrates its **rules and validations**, such as name, email and password. This layer does not know about HTTP, databases, frameworks or any infrastructure detail.

## Dependency rule

Being the innermost layer, `domain` **does not depend on any other layer of the application**. It also has no dependencies on external frameworks or technologies, such as Spring, JPA, Jackson or Servlet.

If a class in `domain` needs to import something from `application`, `infrastructure` or `main`, it is a sign that the responsibility may be in the wrong layer.

## What lives here

* **Entities** (`entity/`): represent the domain concepts and hold their own rules and validations. Example: [`entity/User.java`](entity/User.java), which validates its data on creation and on changes such as `changePassword`.
* **Domain exceptions** (`exception/`): represent errors related to the domain rules. Examples:

  * [`exception/DomainException.java`](exception/DomainException.java): base domain exception.
  * [`exception/DomainValidationException.java`](exception/DomainValidationException.java): used when a domain validation fails, such as an empty email or a password that is too short.

## What does **not** live here

* **Gateway/Repository interfaces**: contracts like `UserGateway` live in `application/`, not in `domain/`. The decision is explained in [`../application/README.md`](../application/README.md#why-the-gateway-lives-in-application-not-in-domain).
* **Use cases / interactors**: belong to the `application/` layer, which is responsible for coordinating the application flows.
* **DTOs**: are not part of the domain. The API input and output DTOs, such as `CreateUserRequest` and `CreatedUserResponse`, live in `infrastructure/`. The `Input` and `Output` used in the communication with the use cases live in `application/`. Each layer keeps its own transfer objects, preventing API details from reaching the domain.
* **Framework annotations**: `domain/` must not depend on annotations such as `@Entity`, `@Component`, `@Service` or `@JsonProperty`. Spring or JPA imports in this layer indicate a coupling that should be avoided.

## Project decisions

### Validations on entity creation

Entities must be created in a valid state, holding their own rules and validations.

In the case of `User`, for example, the constructor validates email, password and name before assigning the values. This way, there is no need to depend on a later call to `validate()` or on validations performed by other layers.

### Validation on changes

The domain rules must also be applied when an entity's state is changed.

In `User`, for example, `changePassword` validates the new password before performing the change. This way, the rules are maintained both on creation and on changes to the entity.

### Domain-specific exceptions

Violations of the domain rules are represented by their own exceptions. Currently, `DomainValidationException` extends `DomainException`, keeping these errors separate from generic language exceptions.

This allows identifying when a domain rule has been violated without coupling the entity to the way this error will be handled externally. In `infrastructure/`, for example, the `GlobalExceptionHandler` can translate these exceptions to the appropriate HTTP response.

## References

- Robert C. Martin, Clean Architecture, chapter 20, "Business Rules".
