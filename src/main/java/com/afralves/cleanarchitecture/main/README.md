# Camada `main`

## Objetivo

`main` é o **composition root** da aplicação. É o único lugar onde as três camadas (`domain`, `application`, `infrastructure`) se encontram, e onde o grafo de dependências é montado explicitamente.

Quando o app sobe, `main` diz: "instancie `CreateUserInteractor` passando um `UserRepositoryAdapter` como `UserGateway`, e esse adapter recebe um `UserRepository` (Spring Data) e um `UserEntityConverter`". Todas as amarrações concretas moram aqui.

## Regra de dependência

- **Para dentro:** conhece **todas** as outras camadas. É a única camada com essa liberdade.
- **Para fora:** conhece Spring (para declarar beans com `@Configuration` / `@Bean`).
- Quem conhece essa camada: ninguém. Nenhum outro pacote importa de `main/`.

`main` é a "cúpula" invertida: no diagrama da Clean Architecture as dependências apontam pra dentro, mas o composition root senta *acima* de tudo, sabendo montar o todo.

## O que vive aqui

- [`UserConfig.java`](UserConfig.java) — classe `@Configuration` com métodos `@Bean` que instanciam:
  - Cada `<Caso>InputBoundary` (implementado pelo `<Caso>Interactor`).
  - O `UserGateway` (implementado pelo `UserRepositoryAdapter`).
  - O `UserEntityConverter`.

## O que **não** vive aqui

- **Regra de negócio.** Nenhuma. `main` só monta objetos.
- **Adapters.** Implementações concretas (controllers, adapters, converters) vivem em `infrastructure/`.
- **Interactors.** Vivem em `application/`.

Se `main` começar a crescer em lógica, alguma coisa está no lugar errado.

## Decisões do projeto

### Beans são declarados manualmente com `@Bean`, não descobertos por `@ComponentScan`

Nenhum interactor ou gateway tem `@Service` / `@Component`. O `UserConfig` instancia cada um explicitamente:

```java
@Bean
CreateUserInputBoundary createUser(UserGateway userGateway) {
    return new CreateUserInteractor(userGateway);
}
```

**Por quê:**

1. **`application/` continua livre de Spring.** Interactors são POJOs testáveis com `new CreateUserInteractor(mockGateway)`, sem contexto.
2. **O grafo fica explícito.** Ler `UserConfig` mostra, em um arquivo, como as peças se conectam. Não precisa caçar `@Autowired` distribuído.
3. **Trocar implementação é uma linha.** Se um dia `UserRepositoryAdapter` for substituído por outra implementação de `UserGateway`, muda o `@Bean userGateway(...)` e pronto — nenhuma outra camada percebe.

### `main` é o único lugar autorizado a fazer o wiring das três camadas

Você **não vai** encontrar `application/` importando `UserRepositoryAdapter` diretamente, nem `infrastructure/` construindo `CreateUserInteractor`. Toda a costura acontece aqui.

**Por quê:** essa é a Dependency Rule funcionando na prática. As camadas internas expõem interfaces (`InputBoundary`, `Gateway`); as camadas externas as implementam; `main` casa as duas. É o único lugar onde os dois lados se enxergam ao mesmo tempo.

### Não há um `@ComponentScan` explícito porque `@SpringBootApplication` cobre

`CleanarchitectureApplication` (na raiz do pacote) tem `@SpringBootApplication`, que já implica `@ComponentScan` a partir dali. Coisas que **precisam** ser descobertas por scan (`@RestController`, `@Repository`, `@RestControllerAdvice` de `infrastructure/`) são pegas automaticamente. Coisas que **não** têm anotação de stereotype (interactors, gateways, converter) são declaradas manualmente aqui.

**Por quê:** misturar `@ComponentScan` amplo com `@Bean` manual gera confusão sobre "de onde vem esse bean". A regra simples: se é adapter framework-anotado, scan pega. Se é código puro de aplicação, `UserConfig` declara.

## Referências

- Mark Seemann, *Dependency Injection Principles, Practices, and Patterns*, cap. "Composition Root" — o conceito de um único ponto de composição, o mais próximo possível do `main`.
- Robert C. Martin, *Clean Architecture*, cap. 26 ("The Main Component") — "Main is the ultimate detail — the lowest-level policy".
