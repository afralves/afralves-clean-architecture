# Camada `domain`

## Objetivo

`domain` é o **núcleo da aplicação**. Aqui ficam as **entidades e as regras de negócio** que devem ser preservadas independentemente de detalhes externos.

No projeto, `User` representa o usuário e concentra suas **regras e validações**, como nome, e-mail e senha. Essa camada não conhece HTTP, banco de dados, frameworks ou qualquer detalhe de infraestrutura.

## Regra de dependência

Por ser a camada mais interna, `domain` **não depende de nenhuma outra camada da aplicação**. Também não possui dependências de frameworks ou tecnologias externas, como Spring, JPA, Jackson ou Servlet.

Se uma classe do `domain` precisar importar algo de `application`, `infrastructure` ou `main`, é um sinal de que a responsabilidade pode estar na camada errada.

## O que vive aqui

* **Entidades** (`entity/`): representam os conceitos do domínio e mantêm suas próprias regras e validações. Exemplo: [`entity/User.java`](entity/User.java), que valida seus dados na criação e em alterações como `changePassword`.
* **Exceções de domínio** (`exception/`): representam erros relacionados às regras do domínio. Exemplos:

  * [`exception/DomainException.java`](exception/DomainException.java): exceção base do domínio.
  * [`exception/DomainValidationException.java`](exception/DomainValidationException.java): usada quando uma validação do domínio falha, como e-mail vazio ou senha muito curta.

## O que **não** vive aqui

* **Gateway/Repository interfaces**: contratos como `UserGateway` ficam em `application/`, não em `domain/`. A decisão é explicada em [`../application/README.md`](../application/README.md#por-que-o-gateway-mora-em-application-não-em-domain).
* **Use cases / interactors**: pertencem à camada `application/`, responsável por coordenar os fluxos da aplicação.
* **DTOs**: não fazem parte do domínio. Os DTOs de entrada e saída da API, como `CreateUserRequest` e `CreatedUserResponse`, ficam em `infrastructure/`. Já os `Input` e `Output` usados na comunicação com os use cases ficam em `application/`. Cada camada mantém seus próprios objetos de transferência, evitando que detalhes da API cheguem ao domínio.
* **Anotações de framework**: `domain/` não deve depender de anotações como `@Entity`, `@Component`, `@Service` ou `@JsonProperty`. Imports de Spring ou JPA nessa camada indicam um acoplamento que deve ser evitado.

## Decisões do projeto

### Validações na criação das entidades

As entidades devem ser criadas em um estado válido, mantendo suas próprias regras e validações.

No caso de `User`, por exemplo, o construtor valida e-mail, senha e nome antes de atribuir os valores. Com isso, não é necessário depender de uma chamada posterior a `validate()` ou de validações feitas por outras camadas.

### Validação nas alterações

As regras do domínio também devem ser aplicadas quando o estado de uma entidade é alterado.

Em `User`, por exemplo, `changePassword` valida a nova senha antes de fazer a alteração. Dessa forma, as regras são mantidas tanto na criação quanto nas alterações da entidade.

### Exceções específicas do domínio

As violações das regras do domínio são representadas por exceções próprias. Atualmente, `DomainValidationException` estende `DomainException`, mantendo esses erros separados de exceções genéricas da linguagem.

Isso permite identificar quando uma regra do domínio foi violada sem acoplar a entidade à forma como esse erro será tratado externamente. Em `infrastructure/`, por exemplo, o `GlobalExceptionHandler` pode traduzir essas exceções para a resposta HTTP adequada.

## Referências

- Robert C. Martin, Clean Architecture, capítulo 20, "Business Rules".
