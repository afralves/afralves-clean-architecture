# Camada `application`

## Objetivo

`application` é a camada de **casos de uso**. Aqui ficam os fluxos específicos da aplicação, como criar, listar ou atualizar usuários.

Os casos de uso coordenam as entidades de domínio e os contratos necessários para executar cada operação. Essa camada define **como o fluxo da aplicação acontece**, sem depender de HTTP, banco de dados ou frameworks como Spring.


## Regra de dependência

* **Para dentro:** `application` pode depender de `domain`.
* **Para fora:** não depende de `infrastructure` ou `main`, nem de frameworks como Spring ou JPA.
* **Quem depende de `application`:** `infrastructure`, que implementa seus contratos, e `main`, que configura as dependências.

## O que vive aqui

* **Use cases / Interactors** (`usecases/`): implementam os fluxos da aplicação. Cada caso de uso mantém seus próprios `InputBoundary`, `Interactor`, `Input` e `Output`.
* **Gateways** (`gateway/`): definem os contratos necessários para que os casos de uso acessem recursos externos. As implementações desses contratos ficam em `infrastructure/`.
* **Exceções de aplicação** (`exceptions/`): representam erros relacionados aos fluxos dos casos de uso, como e-mail já existente ou usuário não encontrado.

## O que **não** vive aqui

* **Implementações de gateways**: os contratos ficam em `application/`, enquanto implementações como `UserRepositoryAdapter` ficam em `infrastructure/`.
* **DTOs de request/response HTTP**: pertencem à camada `infrastructure/`. Os casos de uso trabalham com seus próprios `Input` e `Output`.
* **Dependências de framework**: interactors não utilizam anotações como `@Service`, `@Component`, `@Transactional` ou `@Autowired`. A configuração das dependências é feita em `main/`.
* **Entidades de persistência**: os casos de uso trabalham com entidades de domínio, como `User`, e não com representações de persistência como `UserEntity`.

## Decisões do projeto

### Gateway definido em `application`

Os gateways utilizados pelos casos de uso são definidos em `application`. `UserGateway`, por exemplo, representa as operações externas que os interactors precisam para executar seus fluxos.

A interface fica próxima de quem a utiliza, enquanto sua implementação fica em `infrastructure`. Dessa forma, os casos de uso dependem apenas do contrato e não conhecem detalhes de persistência.

Essa decisão também mantém `domain` focado nas entidades e regras de negócio, sem adicionar contratos relacionados às necessidades dos casos de uso.

### Use cases têm boundaries próprios

Cada `<Caso>InputBoundary` recebe um `Input` e devolve um `Output`. A entidade `User` não é exposta por essa fronteira e permanece interna ao fluxo do caso de uso.

Exemplo em [`CreateUserInteractor.java`](usecases/createuser/CreateUserInteractor.java):

```text
Input
  → dentro do interactor: cria User e valida suas regras
  → gateway persiste User
  → Output volta para o chamador
```

**Por quê:**

1. **Evita acoplamento com o domínio:** alterações em `User`, como a adição de um novo campo, não alteram automaticamente o contrato de entrada ou saída do caso de uso.
2. **Mantém a criação da entidade no fluxo do caso de uso:** o `Input` contém os dados necessários e o interactor trabalha com a entidade de domínio a partir deles.
3. **Não expõe comportamento da entidade:** `User` possui comportamentos como `changePassword`, enquanto `Input` e `Output` representam apenas os dados necessários para a operação.
4. **Entrada, domínio e saída podem ter estruturas diferentes:** nem todo dado recebido precisa fazer parte da entidade e nem todo dado da entidade precisa ser retornado.

O `Input` também pode realizar conversões específicas para o domínio, como [`CreateUserInput.toUser()`](usecases/createuser/CreateUserInput.java), mantendo essa conversão dentro de `application`.

### Records de boundary carregam sua própria conversão

Cada `Input` sabe virar entidade de domínio dentro do próprio record, por exemplo, [`CreateUserInput.toUser()`](usecases/createuser/CreateUserInput.java). Não existe uma classe `InputConverter` externa.

**Por quê:**

* A conversão é específica do próprio record: origem e destino ficam lado a lado, sem indireção.
* O record continua sendo dado puro com uma conversão pequena, sem introduzir uma nova classe apenas para essa responsabilidade.
* Cada record sabe converter seus dados para o próximo passo do fluxo, mantendo a leitura linear (`request → input → domínio → output → response`).

### Gateway usa `User` (domínio), não `Input`/`Output`

A regra "boundary é dado puro" vale para a fronteira **de entrada e de saída** do use case (controller ↔ interactor). O gateway é utilizado internamente pelo use case e trafega `User`, porque quem consome `UserGateway` é o interactor, e o interactor trabalha com o domínio.

O adapter em `infrastructure/` é responsável pela conversão entre `User` e `UserEntity` (JPA).

### Interactors são POJOs sem anotação de framework

Nenhum `@Service`, `@Component`, `@Autowired` ou `@Transactional` aparece nesta camada. Interactors são classes puras, com dependências injetadas via construtor.

**Por quê:** manter o framework fora desta camada reduz o acoplamento com Spring. A configuração fica em `main/UserConfig`, que declara os beans manualmente via `@Bean`. Os interactors também podem ser testados diretamente com `new CreateUserInteractor(mockGateway)`, sem subir o contexto do Spring.

## Referências

- Robert C. Martin, Clean Architecture, capítulo 22, "The Clean Architecture", incluindo a seção "What Data Crosses the Boundaries".