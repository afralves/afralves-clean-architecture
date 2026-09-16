# Camada `infrastructure`

## Objetivo

`infrastructure` é a camada responsável pelos **adapters** e pela comunicação da aplicação com recursos externos, como HTTP e banco de dados.

Aqui ficam as implementações que utilizam frameworks e tecnologias externas, como Spring MVC e Spring Data JPA. Esses componentes fazem a ligação entre as interfaces externas e os contratos definidos pelas camadas internas, como `InputBoundary` e `Gateway`.

## Regra de dependência

`infrastructure` pode depender de `application` e `domain` para utilizar seus contratos e objetos. Também concentra dependências de frameworks e tecnologias externas, como Spring MVC, Spring Data JPA e Jackson.

As camadas `application` e `domain` **não dependem de `infrastructure`**. Quando uma implementação externa é necessária, `infrastructure` implementa o contrato definido pela camada interna e `main` fica responsável por conectar os dois.

## O que vive aqui

### `adapter/controller/`

Responsável pela entrada HTTP, incluindo controllers e DTOs de request/response.

### `adapter/persistence/`

Responsável pelo acesso aos dados e pela implementação dos gateways definidos em `application`.

### `adapter/exception/`

Responsável pelo tratamento das exceções e pela conversão dos erros da aplicação em respostas HTTP.

## O que **não** vive aqui

* **Regras de negócio**: ficam nas camadas internas, principalmente em `domain` e nos fluxos coordenados por `application`.
* **Contratos abstratos**: interfaces como `UserGateway` ficam em `application/`. Em `infrastructure/` fica sua implementação, como `UserRepositoryAdapter`.
* **Entidades de domínio anotadas com JPA**: `User` permanece independente de persistência em `domain/`, enquanto `UserEntity` representa os dados persistidos em `infrastructure/`. A conversão entre as duas é feita pelo converter.

## Decisões do projeto

### Entidade JPA é separada da entidade de domínio

`User` (domínio) e `UserEntity` (JPA) são classes diferentes, e o `UserEntityConverter` faz a conversão entre elas.

**Por quê:** anotações como `@Entity`, `@Column` e `@Id` são detalhes de persistência e não fazem parte do domínio. Manter as classes separadas evita que `domain/` dependa de JPA e mantém `User` independente da forma como seus dados são persistidos.

### Records de request/response carregam sua própria conversão

A mesma decisão adotada em `application/` é utilizada aqui:

* [`CreateUserRequest.toCreateUserInput()`](adapter/controller/request/CreateUserRequest.java): converte o request HTTP para o `Input` do use case.
* [`CreatedUserResponse.from(output)`](adapter/controller/response/CreatedUserResponse.java) e [`ListUserResponse.from(outputs)`](adapter/controller/response/ListUserResponse.java): constroem o response HTTP a partir do `Output` do use case.

Como são conversões simples e específicas desses records, não utilizamos classes adicionais como `RequestConverter` ou `ResponseConverter`.

A mesma abordagem é explicada em [`../application/README.md`](../application/README.md#records-de-boundary-carregam-sua-própria-conversão).

### Conversão entre domínio e persistência

Diferente dos records de request/response e input/output, que carregam a própria conversão no formato `.from(...)` / `.toX()`, a fronteira JPA ↔ domínio utiliza uma classe separada. Nesse caso, a conversão vive fora dos próprios tipos.

A lógica de conversão entre `User` (domínio) e `UserEntity` (JPA) mora em uma classe própria, injetada no `UserRepositoryAdapter` pelo construtor.

* **Responsabilidade única:** o adapter fica responsável por orquestrar as operações de persistência, enquanto o converter cuida exclusivamente da conversão entre `User` e `UserEntity`.
* **Evita duplicação:** a lógica de conversão é centralizada em um único lugar e reutilizada por operações como `saveUser`, `findUsers` e `findByEmail`.
* **Testabilidade:** a conversão pode ser testada isoladamente, sem depender de JPA, repository ou banco de dados.
* **`UserEntity` é uma entidade JPA:** é uma classe mutável e anotada pelo framework (`@Entity`, `@Column`). Colocar a lógica de conversão dentro dela adicionaria outra responsabilidade à entidade; o converter externo mantém `UserEntity` focada na representação de persistência.

### Request/Response records ficam por endpoint

Cada endpoint possui seus próprios records de request e response, em vez de compartilhar um `UserDto` entre diferentes operações.

**Por quê:** cada endpoint possui seu próprio contrato e pode exigir dados diferentes. `password`, por exemplo, faz sentido em `CreateUserRequest`, mas não em `CreatedUserResponse`. Manter DTOs separados evita campos desnecessários ou opcionais e permite que cada operação evolua de forma independente.

### Tratamento centralizado de exceções

O `GlobalExceptionHandler` centraliza a conversão das exceções conhecidas de `domain` e `application` para respostas HTTP. Cada tipo de exceção é associado ao status correspondente, mantendo esse tratamento fora dos controllers.

Exceções não mapeadas são tratadas por um fallback com `@ExceptionHandler(Exception.class)`, que registra o erro e retorna uma resposta `500` padronizada sem expor detalhes internos ao cliente.

## Referências

* Robert C. Martin, *Clean Architecture*, capítulo 22, "The Clean Architecture".
