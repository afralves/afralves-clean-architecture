# Camada `infrastructure`

## Objetivo

`infrastructure` é a camada de **adapters**. Ela conecta a aplicação (use cases + domínio) ao mundo externo: HTTP, banco de dados, filas, integrações. Toda interação com framework (Spring MVC, Spring Data JPA) e toda dependência concreta (driver JDBC, serializador JSON) vive aqui.

O termo "adapter" vem literalmente da Hexagonal Architecture: essa camada **adapta** contratos externos (uma URL HTTP, uma tabela relacional) para contratos internos (`InputBoundary`, `Gateway`). Ela traduz.

## Regra de dependência

- **Para dentro:** pode conhecer `application/` e `domain/`. Implementa interfaces declaradas por elas.
- **Para fora:** conhece frameworks (Spring, JPA, Jackson) e o protocolo externo (HTTP).
- Quem conhece essa camada: só `main/`, que faz o wiring de beans.

Nenhum arquivo em `application/` ou `domain/` importa nada de `infrastructure/`. A dependência é sempre pra dentro.

## O que vive aqui

### `adapter/controller/` — entrada HTTP

- [`UserController.java`](adapter/controller/UserController.java) — mapeia rotas REST para chamadas ao `InputBoundary` correspondente. Não contém lógica de negócio, só delega.
- `adapter/controller/request/` — records de entrada (`CreateUserRequest`, `UpdateUserRequest`). Sabem converter pra `Input` do use case (`request.toCreateUserInput()`).
- `adapter/controller/response/` — records de saída (`CreatedUserResponse`, `ListUserResponse`, `UserListItemResponse`). Sabem ser construídos a partir do `Output` do use case (`CreatedUserResponse.from(output)`).

### `adapter/persistence/` — saída para banco

- [`UserRepositoryAdapter.java`](adapter/persistence/UserRepositoryAdapter.java) — implementa `UserGateway` (declarado em `application/`). Traduz `User` (domínio) ↔ `UserEntity` (JPA) e delega ao Spring Data.
- `adapter/persistence/repository/UserRepository.java` — interface Spring Data (`ListCrudRepository<UserEntity, Long>`). Detalhe de framework, isolado aqui.
- `adapter/persistence/model/UserEntity.java` — entidade JPA (`@Entity`, `@Table`, `@Column`). É um espelho anêmico do `User` para persistência.
- `adapter/persistence/converter/UserEntityConverter.java` — tradução `User` ↔ `UserEntity`. Bean próprio, injetado no adapter.

### `adapter/exception/` — mapeamento de erro para HTTP

- [`GlobalExceptionHandler.java`](adapter/exception/GlobalExceptionHandler.java) — `@RestControllerAdvice` que captura exceções de `domain/` e `application/` e as converte em respostas HTTP.
- [`ErrorCode.java`](adapter/exception/ErrorCode.java) — enum que associa cada erro conhecido a um `HttpStatus`.
- [`ErrorResponse.java`](adapter/exception/ErrorResponse.java) — record do corpo de erro padronizado (`timestamp`, `status`, `code`, `message`).

## O que **não** vive aqui

- **Regra de negócio.** Se um `if` no controller decide algo específico do negócio, esse `if` está no lugar errado — mova pro interactor.
- **Contratos abstratos.** Interfaces como `UserGateway` moram em `application/`. Aqui vive só a implementação (`UserRepositoryAdapter`).
- **A entidade de domínio anotada com JPA.** `User` é puro em `domain/`; `UserEntity` (anotada) é uma classe separada aqui. A tradução é feita pelo converter.

## Decisões do projeto

### Entidade JPA é separada da entidade de domínio

`User` (domínio) e `UserEntity` (JPA) são classes diferentes, e o `UserEntityConverter` faz a tradução entre elas.

**Por quê:** anotações como `@Entity`, `@Column`, `@Id` são detalhes de framework. Se `User` fosse anotado com JPA:

1. `domain/` passaria a depender de `jakarta.persistence`. Vazamento clássico.
2. A entidade de domínio ficaria refém do ciclo de vida da JPA (proxies, lazy loading, `equals/hashCode` de identidade Hibernate).
3. Trocar Hibernate por outra coisa (jOOQ, Micronaut Data, um banco de documentos) exigiria mexer no domínio.

O custo é um converter e duas classes. O ganho é isolamento real.

### `UserEntityConverter` é bean próprio, não classe utilitária estática

O converter é injetado no `UserRepositoryAdapter` pelo construtor, e é declarado como `@Bean` em [`main/UserConfig`](../main/UserConfig.java).

**Por quê:** manter a construção via injeção deixa o adapter testável sem instanciar toda a cadeia manualmente, e uniformiza o padrão — todo componente é bean, sem exceções.

### Handler de exceção mapeia por tipo, não por status genérico

`GlobalExceptionHandler` tem um `@ExceptionHandler` por tipo de exceção conhecida (`DomainValidationException` → 400, `EmailAlreadyExistsException` → 409, `UserNotFoundException` → 404). Isso só funciona porque `domain/` e `application/` expõem hierarquias de exceção específicas em vez de `RuntimeException` genérica.

**Por quê:** o mapeamento é declarativo e centralizado. Adicionar um novo erro de aplicação: cria a exceção em `application/exceptions/`, adiciona uma entrada em `ErrorCode`, adiciona um handler no `GlobalExceptionHandler`. O controller não muda.

### Fallback genérico existe, e é intencionalmente último

`@ExceptionHandler(Exception.class)` captura qualquer coisa não mapeada e devolve `INTERNAL_ERROR` (500). Logamos com stack trace, mas nunca vazamos a mensagem original — o cliente recebe `"An unexpected error occurred."`.

**Por quê:** exceção não-mapeada é bug. Não podemos deixar o Spring devolver `500` com HTML padrão ou vazar `NullPointerException` no corpo. Nem é aceitável estourar sem log — precisamos investigar.

### Request/Response records ficam por endpoint, não compartilhados

Cada endpoint tem seu próprio par de records. Não temos um `UserDto` compartilhado entre "criar", "listar", "atualizar".

**Por quê:** cada endpoint tem contrato próprio. Um `password` faz sentido em `CreateUserRequest`, não em `CreatedUserResponse`. Compartilhar DTOs entre operações leva a campos opcionais, `@JsonIgnore` seletivos, e a tentação de reusar entidade de domínio como DTO.

## Referências

- Robert C. Martin, *Clean Architecture*, cap. 22 ("The Clean Architecture"), o círculo verde ("Interface Adapters") — traduz de/para formatos de fronteira.
- Alistair Cockburn, *Hexagonal Architecture* — a origem do termo "adapter" no sentido usado aqui.
