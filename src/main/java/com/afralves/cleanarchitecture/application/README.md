# `application` layer

## Objective

`application` is the **use cases** layer. This is where the application-specific flows live, such as creating, listing or updating users.

The use cases coordinate the domain entities and the contracts required to execute each operation. This layer defines **how the application flow happens**, without depending on HTTP, database or frameworks such as Spring.

## Dependency rule

* **Inward:** `application` may depend on `domain`.
* **Outward:** it does not depend on `infrastructure` or `main`, nor on frameworks such as Spring or JPA.
* **Who depends on `application`:** `infrastructure`, which implements its contracts, and `main`, which configures the dependencies.

## What lives here

* **Use cases / Interactors** (`usecases/`): implement the application flows. Each use case holds its own `InputBoundary`, `Interactor`, `Input` and `Output`.
* **Gateways** (`gateway/`): define the contracts required for the use cases to access external resources. The implementations of these contracts live in `infrastructure/`.
* **Application exceptions** (`exceptions/`): represent errors related to the use case flows, such as an email that already exists or a user that was not found.

## What does **not** live here

* **Gateway implementations**: the contracts live in `application/`, while implementations such as `UserRepositoryAdapter` live in `infrastructure/`.
* **HTTP request/response DTOs**: belong to the `infrastructure/` layer. The use cases work with their own `Input` and `Output`.
* **Framework dependencies**: interactors do not use annotations such as `@Service`, `@Component`, `@Transactional` or `@Autowired`. The configuration of dependencies is done in `main/`.
* **Persistence entities**: the use cases work with domain entities, such as `User`, and not with persistence representations such as `UserEntity`.

## Project decisions

### Gateway defined in `application`

The gateways used by the use cases are defined in `application`. `UserGateway`, for example, represents the external operations that the interactors need to execute their flows.

The interface stays close to who uses it, while its implementation lives in `infrastructure`. This way, the use cases depend only on the contract and do not know persistence details.

This decision also keeps `domain` focused on the entities and business rules, without adding contracts related to the use cases' needs.

### Use cases have their own boundaries

Each `<Case>InputBoundary` receives an `Input` and returns an `Output`. The `User` entity is not exposed by this boundary and remains internal to the use case flow.

Example in [`CreateUserInteractor.java`](usecases/createuser/CreateUserInteractor.java):

```text
Input
  → inside the interactor: creates User and validates its rules
  → gateway persists User
  → Output returns to the caller
```

**Why:**

1. **Avoids coupling with the domain:** changes in `User`, such as adding a new field, do not automatically change the input or output contract of the use case.
2. **Keeps the entity creation inside the use case flow:** the `Input` contains the required data and the interactor works with the domain entity from it.
3. **Does not expose the entity's behavior:** `User` has behaviors such as `changePassword`, while `Input` and `Output` represent only the data required for the operation.
4. **Input, domain and output may have different structures:** not every received data needs to be part of the entity, and not every entity data needs to be returned.

The `Input` may also perform conversions specific to the domain, such as [`CreateUserInput.toUser()`](usecases/createuser/CreateUserInput.java), keeping this conversion inside `application`.

### Boundary records carry their own conversion

Each `Input` knows how to become a domain entity inside its own record, for example, [`CreateUserInput.toUser()`](usecases/createuser/CreateUserInput.java). There is no external `InputConverter` class.

**Why:**

* The conversion is specific to the record itself: source and destination sit side by side, without indirection.
* The record remains pure data with a small conversion, without introducing a new class only for this responsibility.
* Each record knows how to convert its data to the next step of the flow, keeping the reading linear (`request → input → domain → output → response`).

### Gateway uses `User` (domain), not `Input`/`Output`

The rule "boundary is pure data" applies to the use case's **input and output** boundary (controller ↔ interactor). The gateway is used internally by the use case and carries `User`, because the consumer of `UserGateway` is the interactor, and the interactor works with the domain.

The adapter in `infrastructure/` is responsible for the conversion between `User` and `UserEntity` (JPA).

### Interactors are POJOs without framework annotations

No `@Service`, `@Component`, `@Autowired` or `@Transactional` appears in this layer. Interactors are pure classes, with dependencies injected via constructor.

**Why:** keeping the framework out of this layer reduces coupling with Spring. The configuration lives in `main/UserConfig`, which declares the beans manually via `@Bean`. The interactors can also be tested directly with `new CreateUserInteractor(mockGateway)`, without starting the Spring context.

## References

- Robert C. Martin, Clean Architecture, chapter 22, "The Clean Architecture", including the section "What Data Crosses the Boundaries".
