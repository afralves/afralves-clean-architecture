# Camada `application`

## Objetivo

`application` é a camada de **casos de uso**. Aqui vivem as regras que dizem o que a aplicação **faz** — orquestrações específicas do sistema, executadas em resposta a alguém "querendo alguma coisa" (criar usuário, listar usuários, trocar senha).

Um caso de uso combina entidades de domínio com contratos abstratos do mundo externo (gateways) para realizar uma operação. Ele não sabe de HTTP, não sabe de SQL, não sabe de Spring — mas sabe **qual é o fluxo** do caso de uso.

## Regra de dependência

- **Para dentro:** pode conhecer `domain/`.
- **Para fora:** **não** conhece `infrastructure/` nem `main/`. Não conhece Spring, JPA, Jackson, Servlet.
- Quem conhece essa camada: `infrastructure/` (implementa as interfaces que ela declara) e `main/` (fábrica os beans).

## O que vive aqui

- **Use cases / Interactors** (`usecases/<caso>/`) — implementação do fluxo. Um use case por pacote, com quatro arquivos:
  - `<Caso>InputBoundary.java` — interface do que o caso oferece pra quem chama.
  - `<Caso>Interactor.java` — implementação do fluxo.
  - `<Caso>Input.java` — record da entrada (dado puro).
  - `<Caso>Output.java` — record da saída (dado puro).

  Exemplo: [`usecases/createuser/`](usecases/createuser/) — [`CreateUserInputBoundary`](usecases/createuser/CreateUserInputBoundary.java), [`CreateUserInteractor`](usecases/createuser/CreateUserInteractor.java), [`CreateUserInput`](usecases/createuser/CreateUserInput.java), [`CreateUserOutput`](usecases/createuser/CreateUserOutput.java).

- **Gateways** (`gateway/`) — interfaces que **declaram** o que o caso de uso precisa do mundo externo (persistir usuário, enviar e-mail, publicar evento). Não implementam nada. Exemplo: [`gateway/UserGateway.java`](gateway/UserGateway.java).

- **Exceções de aplicação** (`exceptions/`) — sinalizam falhas de fluxo de caso de uso (e-mail já existe, usuário não encontrado). Diferentes das exceções de `domain/`. Exemplos:
  - [`exceptions/ApplicationException.java`](exceptions/ApplicationException.java) — raiz abstrata.
  - [`exceptions/EmailAlreadyExistsException.java`](exceptions/EmailAlreadyExistsException.java) — conflito em criação.
  - [`exceptions/UserNotFoundException.java`](exceptions/UserNotFoundException.java), [`exceptions/EmailNotFoundException.java`](exceptions/EmailNotFoundException.java) — lookup falhou.

## O que **não** vive aqui

- **Implementação de gateway.** A interface fica aqui; a implementação (`UserRepositoryAdapter`) mora em `infrastructure/`.
- **DTOs de request/response HTTP.** Esses moram em `infrastructure/adapter/controller/{request,response}/`.
- **`@Service`, `@Component`, `@Transactional`, `@Autowired`.** Interactors são POJOs, instanciados por construtor. Quem faz a amarração é `main/`.
- **Referência direta a `UserEntity` (JPA).** Interactors só conhecem `User` (domínio). A tradução domínio ↔ entidade JPA acontece no adapter.

## Decisões do projeto

### Por que o gateway mora em `application`, não em `domain`

Esta é a decisão mais importante da camada. Estamos seguindo **Clean Architecture (Uncle Bob) estrita**, não DDD clássico.

- No **DDD** (Evans), o Repository é um conceito de domínio: o especialista de negócio entende "repositório de clientes". Faz sentido no universo da modelagem rica de domínios complexos.
- No **Clean Architecture puro**, a regra é universal: *a interface pertence a quem a usa*. Quem usa `UserGateway` é o use case (`CreateUserInteractor`), então o contrato vive junto do use case, em `application/gateway/`.

Consequências dessa escolha:

1. **Domínio fica realmente puro.** Se o gateway estivesse em `domain/`, a camada mais interna conheceria o conceito abstrato de "persistência". No Uncle Bob puro, domínio não sabe que existe *nada* fora dele — nem banco, nem que os dados serão salvos em algum lugar. É a entidade pela entidade.
2. **Regra "interface pertence a quem usa" fica universal.** No DDD tem exceção (repository); no Uncle Bob não. Menos casos especiais pra lembrar quando decidir onde algo vai.
3. **Use case fica autocontido.** Abrindo `application/` você vê: aqui estão os casos de uso + os contratos que eles precisam do mundo externo. Não precisa navegar pra outra camada pra entender o que o use case consome.

### Use cases têm boundaries próprios — controller nunca conhece `User`

Cada `<Caso>InputBoundary` recebe um `Input` (record com dado puro) e devolve um `Output` (record com dado puro). A entidade `User` **não** atravessa a fronteira do use case pra fora — ela nasce e morre dentro do `Interactor`.

Exemplo em [`CreateUserInteractor.java`](usecases/createuser/CreateUserInteractor.java):

```
Input (record puro)
  → dentro do interactor: new User(...) valida invariantes
  → gateway persiste User
  → Output (record puro) volta pro chamador
```

**Por quê:**

1. **Acoplamento estrutural evitado.** Se adicionar um campo em `User` (ex: `phoneNumber`), o controller compila normal e não expõe o campo por acidente. Entidade e contrato HTTP não ficam presos um ao outro.
2. **Invariantes disparam dentro da camada correta.** `User` valida no construtor. Se o controller construísse `User` diretamente, a `DomainValidationException` estouraria na camada de apresentação, antes de o use case ser chamado. Passando `Input` (record puro, sem validação), a validação é responsabilidade do use case ao construir a entidade.
3. **Comportamento vs. dado.** Entidade tem comportamento (`changePassword`). O que atravessa fronteira é dado puro (record). O controller não deveria ter acesso a `user.changePassword(...)`.
4. **Assimetria input/output.** Nem todo campo do request vira campo do domínio; nem todo campo do domínio deve virar response. Trafegar `User` cru força os dois lados a coincidirem.

O `Input` sabe converter pra `User` ([`CreateUserInput.toUser()`](usecases/createuser/CreateUserInput.java)) porque essa tradução é assunto interno de `application` — o record vive na camada que sabe o que fazer com ele.

### Records de boundary carregam sua própria conversão

Cada `Input` sabe virar entidade de domínio dentro do próprio record — por exemplo, [`CreateUserInput.toUser()`](usecases/createuser/CreateUserInput.java). Não existe uma classe `InputConverter` externa.

**Por quê:**

- A conversão é one-shot e ligada ao próprio record — origem e destino ficam lado a lado, sem indireção.
- O record continua sendo dado puro com uma fábrica pequena; não introduzimos uma nova classe só pra empacotar uma linha de tradução.
- Cada record é responsável por saber virar o próximo passo do fluxo, mantendo a leitura linear (`request → input → domínio → output → response`).

### Gateway usa `User` (domínio), não `Input`/`Output`

A regra "boundary é dado puro" vale pra fronteira **de entrada e de saída** do use case (controller ↔ interactor). O gateway é uma porta **de dentro** do use case — ele trafega `User` mesmo, porque quem consome `UserGateway` é o interactor, e o interactor pensa em domínio.

O adapter em `infrastructure/` é que traduz `User` ↔ `UserEntity` (JPA).

### Interactors são POJOs sem anotação de framework

Nenhum `@Service`, `@Component`, `@Autowired`, `@Transactional` aparece nesta camada. Interactors são classes puras, com dependências injetadas via construtor.

**Por quê:** trocar de framework (ou remover completamente, para testar) não deve tocar nesta camada. A amarração com Spring vive em `main/UserConfig`, que declara os beans manualmente via `@Bean`. Interactors ficam testáveis com um `new CreateUserInteractor(mockGateway)` — sem contexto Spring.

## Referências

- Robert C. Martin, *Clean Architecture*, cap. 22 ("The Clean Architecture") — a distinção entre Entities, Use Cases, Interface Adapters e Frameworks & Drivers.
- Robert C. Martin, *Clean Architecture*, cap. 22, seção "What Data Crosses the Boundaries" — "we don't want to cheat and pass Entity objects […] across a boundary".
