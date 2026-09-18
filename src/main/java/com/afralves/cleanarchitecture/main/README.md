# `main` layer

## Objective

`main` is the application's **composition root**. It is where the dependencies between `domain`, `application` and `infrastructure` are configured and the application is prepared for execution.

In this layer, the configurations that connect the **concrete implementations** to the **abstractions** defined by the inner layers are done. For example, an interactor from `application` may receive a gateway implementation provided by `infrastructure`.

This way, `application` still depends only on its abstractions, while `main` is responsible for defining which implementations will be used.

## Dependency rule

`main` may depend on `domain`, `application` and `infrastructure`, since it is responsible for configuring and connecting these layers.

It may also depend on Spring to perform this configuration, using resources such as `@Configuration` and `@Bean`.

The other layers **do not depend on `main`**. This way, the configurations required to assemble the application stay isolated in this layer.

## What lives here

* **Dependency configurations**: responsible for creating the beans and connecting the application's abstractions to their implementations. Currently, [`UserConfig.java`](UserConfig.java) concentrates the configurations related to `User`.

## What does **not** live here

* **Business rules**: belong to `domain` and are not part of the `main` configurations.
* **Adapters**: controllers, persistence adapters and converters live in `infrastructure/`.
* **Interactors**: implement the use cases and live in `application/`.

## Project decisions

### Interactors are configured manually

The interactors in `application` do not use annotations such as `@Service` or `@Component`. They are instantiated in `main` via `@Bean`.

```java
@Bean
CreateUserInputBoundary createUser(UserGateway userGateway) {
    return new CreateUserInteractor(userGateway);
}
```

This decision keeps `application` independent from Spring and makes it explicit where its dependencies are configured. The interactors can also be instantiated directly in tests, without depending on the framework context.

### Wiring centralized in `main`

The configuration that connects the abstractions to their implementations lives in `main`.

For example, the `InputBoundary` defined in `application` are associated with their interactors, while gateways such as `UserGateway` receive their implementations from `infrastructure`.

With this, `application` does not need to know the concrete implementations used by the application.


## References

* Robert C. Martin, *Clean Architecture*, chapter 26, "The Main Component".

