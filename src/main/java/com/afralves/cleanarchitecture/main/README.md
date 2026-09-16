# Camada `main`

## Objetivo

`main` é o **composition root** da aplicação. É onde as dependências entre `domain`, `application` e `infrastructure` são configuradas e a aplicação é preparada para execução.

Nesta camada são feitas as configurações que conectam as **implementações concretas** às **abstrações** definidas pelas camadas internas. Por exemplo, um interactor de `application` pode receber uma implementação de gateway fornecida por `infrastructure`.

Dessa forma, `application` continua dependendo apenas de suas abstrações, enquanto `main` fica responsável por definir quais implementações serão utilizadas.

## Regra de dependência

`main` pode depender de `domain`, `application` e `infrastructure`, pois é responsável por configurar e conectar essas camadas.

Também pode depender do Spring para realizar essa configuração, utilizando recursos como `@Configuration` e `@Bean`.

As demais camadas **não dependem de `main`**. Dessa forma, as configurações necessárias para montar a aplicação ficam isoladas nesta camada.

## O que vive aqui

## O que vive aqui

* **Configurações de dependências**: responsáveis por criar os beans e conectar as abstrações da aplicação às suas implementações. Atualmente, [`UserConfig.java`](UserConfig.java) concentra as configurações relacionadas a `User`.

## O que **não** vive aqui

* **Regras de negócio**: pertencem ao `domain` e não fazem parte das configurações de `main`.
* **Adapters**: controllers, adapters de persistência e converters ficam em `infrastructure/`.
* **Interactors**: implementam os casos de uso e ficam em `application/`.

## Decisões do projeto

### Interactors são configurados manualmente

Os interactors de `application` não utilizam anotações como `@Service` ou `@Component`. Eles são instanciados em `main` por meio de `@Bean`.

```java
@Bean
CreateUserInputBoundary createUser(UserGateway userGateway) {
    return new CreateUserInteractor(userGateway);
}
```

Essa decisão mantém `application` independente do Spring e deixa explícito onde suas dependências são configuradas. Os interactors também podem ser instanciados diretamente nos testes, sem depender do contexto do framework.

### Wiring centralizado em `main`

A configuração que conecta as abstrações às suas implementações fica em `main`.

Por exemplo, os `InputBoundary` definidos em `application` são associados aos seus interactors, enquanto gateways como `UserGateway` recebem suas implementações de `infrastructure`.

Com isso, `application` não precisa conhecer as implementações concretas utilizadas pela aplicação.


## Referências

* Robert C. Martin, *Clean Architecture*, capítulo 26, "The Main Component".

