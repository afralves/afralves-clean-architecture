# Camada `domain`

## Objetivo

`domain` é o núcleo da aplicação. Aqui vivem os conceitos do negócio — o que a aplicação **é**, não o que ela **faz** com o mundo externo. É a camada mais interna da Clean Architecture: as entidades e suas invariantes.

Uma entidade de domínio representa uma regra que existiria mesmo que a aplicação não existisse. O `User` continua sendo `User` (com nome, e-mail, senha, restrições de validade) independente de HTTP, banco, framework, filas.

## Regra de dependência

- **Para dentro:** não existe. É a camada mais interna.
- **Para fora:** nada. `domain` **não conhece** nenhuma outra camada. Não importa `application`, não importa `infrastructure`, não importa `main`, não importa Spring, JPA, Jackson, Servlet.

Se o teste `import` de um arquivo aqui puxar qualquer pacote externo ao próprio `domain`, é um sinal de vazamento — provavelmente o tipo está no lugar errado.

## O que vive aqui

- **Entidades** (`entity/`) — objetos com identidade e comportamento próprios. Exemplo: [`entity/User.java`](entity/User.java). Validam suas próprias invariantes no construtor e em mutações (`changePassword`).
- **Exceções de domínio** (`exception/`) — sinalizam violações de regra do próprio negócio, sem qualquer acoplamento a mecanismos externos. Exemplos:
  - [`exception/DomainException.java`](exception/DomainException.java) — raiz abstrata.
  - [`exception/DomainValidationException.java`](exception/DomainValidationException.java) — violação de invariante (e-mail vazio, senha curta, etc.).

## O que **não** vive aqui

- **Gateway/Repository interfaces.** Contratos como `UserGateway` vivem em `application/`, não aqui. Ver [`../application/README.md`](../application/README.md#por-que-o-gateway-mora-em-application-não-em-domain) para a justificativa.
- **Use cases / interactors.** São de `application/`. Domínio não orquestra — só existe.
- **DTOs de request/response.** São detalhe de fronteira externa, moram em `infrastructure/`.
- **Anotações de framework** (`@Entity`, `@Component`, `@Service`, `@JsonProperty`, etc.). Se aparecer algum import de `org.springframework.*` ou `jakarta.persistence.*` dentro de `domain/`, o commit está errado.

## Decisões do projeto

### Invariantes disparam no construtor

Todo `User` construído é obrigatoriamente válido — o construtor chama `validateEmail`, `validatePassword`, `validateName` antes de atribuir. Não existe `User` "meio pronto".

**Por quê:** é impossível de construir um estado ilegal. Nenhum caller precisa saber "ah, depois de criar preciso chamar `.validate()`". A garantia é do próprio tipo. Uma entidade em domínio não confia no chamador — ela se autopreserva.

### Mutação também valida

`changePassword` também chama `validatePassword`. A entidade se protege contra estados ilegais **em todos** os pontos de entrada, não só no construtor.

### Exceções são específicas, não genéricas

`DomainValidationException` é uma subclasse de `DomainException`, que por sua vez estende `RuntimeException`. Ela **não** é `IllegalArgumentException` genérica.

**Por quê:** o `GlobalExceptionHandler` (em `infrastructure/`) mapeia cada tipo de exceção para um status HTTP específico. Se domínio jogasse `IllegalArgumentException`, o handler não teria como distinguir "regra de negócio violada" de "bug no código". Exceção tipada de domínio é o contrato pra fronteira externa reagir corretamente.

### `id` é opcional na construção

O construtor de `User` aceita `id = null` (usuário ainda não persistido). O `id` é atribuído pelo mundo externo (banco) e volta pra dentro. Domínio não fabrica identidade.

**Por quê:** identidade persistida é um contrato de fronteira, não uma invariante de domínio. Um `User` sem `id` ainda é um `User` legítimo — só ainda não foi salvo.

## Referências

- Robert C. Martin, *Clean Architecture*, cap. 20 ("Business Rules") — a distinção entre Enterprise Business Rules (entidades) e Application Business Rules (use cases).
