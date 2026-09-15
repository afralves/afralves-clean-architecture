# REST API — Clean Architecture com Spring Boot

![Java](https://img.shields.io/badge/Java-21-orange?logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.3-brightgreen?logo=springboot&logoColor=white)
![REST](https://img.shields.io/badge/API-REST-red?logo=fastapi&logoColor=white)
![Maven](https://img.shields.io/badge/Maven-Build-blue?logo=apachemaven&logoColor=white)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-16-blue?logo=postgresql&logoColor=white)
![Flyway](https://img.shields.io/badge/Flyway-Migrations-red?logo=flyway&logoColor=white)

> **REST API** implementada seguindo os princípios de **Clean Architecture**, utilizando **Spring Boot 3.5.3**, com persistência em **PostgreSQL** (via Docker) e migrations gerenciadas pelo **Flyway**, organizada por camadas de domínio, aplicação e infraestrutura.

---

## 📦 Tecnologias Utilizadas

| Tecnologia          | Versão                |
| ------------------- | --------------------- |
| **Java**            | 21                    |
| **Spring Boot**     | 3.5.3                 |
| **PostgreSQL**      | 16 (Docker)           |
| **Spring Web**      | BOM Spring Boot       |
| **Spring Data JPA** | BOM Spring Boot       |
| **Flyway**          | BOM Spring Boot       |
| **Lombok**          | BOM Spring Boot       |
| **Testcontainers**  | BOM Spring Boot       |

---

## 🧱 Estrutura do Projeto

O projeto segue os princípios da **Clean Architecture**, separando responsabilidades por camadas:

```text
com.afralves.cleanarchitecture
├── domain
│   ├── entity
│   └── exception
├── application
│   ├── usecases
│   │   ├── createuser
│   │   ├── deleteuser
│   │   ├── listusers
│   │   └── updateuserpassword
│   ├── gateway
│   └── exceptions
├── infrastructure
│   └── adapter
│       ├── controller
│       │   ├── request
│       │   └── response
│       ├── persistence
│       │   ├── repository
│       │   ├── model
│       │   └── converter
│       └── exception
└── main               (beans / configuração do app)
```

---

## 🚀 Como executar

**1. Clone o repositório:**

```bash
git clone https://github.com/seu-usuario/cleanarchitecture.git
cd cleanarchitecture
```

**2. Suba o banco PostgreSQL (Docker):**

```bash
make db-up
```

> O container **não** sobe automaticamente com o Docker (`restart: "no"`). Suba manualmente sempre que for desenvolver e derrube com `make db-down` quando terminar.

| Comando         | Ação                                    |
| --------------- | --------------------------------------- |
| `make db-up`    | Sobe o Postgres em background           |
| `make db-down`  | Derruba o container                     |
| `make db-logs`  | Segue os logs do Postgres               |
| `make db-status`| Mostra o status do container            |

| Config       | Valor        |
| ------------ | ------------ |
| **Host**     | `localhost`  |
| **Porta**    | `55432`      |
| **Database** | `cleanarch`  |
| **User**     | `cleanarch`  |
| **Password** | `cleanarch`  |

**3. Compile e rode o projeto:**

```bash
./mvnw spring-boot:run
```

O Flyway aplica automaticamente as migrations de `src/main/resources/db/migration/` no startup.

**4. Acesse a aplicação:**

| Recurso    | URL                     |
| ---------- | ----------------------- |
| 🌐 **API** | `http://localhost:8888` |

---

## 💻 Exemplo de uso da API via `curl`

<details>
<summary><strong>🔸 Criar usuário</strong></summary>

```bash
curl -X POST http://localhost:8888/rest/v1/users \
  -H "Content-Type: application/json" \
  -d '{
    "email": "exemplo@teste.com",
    "password": "123456",
    "name": "João da Silva"
  }'
```

</details>

<details>
<summary><strong>🔸 Atualizar senha do usuário</strong></summary>

```bash
curl -X PUT http://localhost:8888/rest/v1/users \
  -H "Content-Type: application/json" \
  -d '{
    "email": "exemplo@teste.com",
    "password": "novaSenha123"
  }'
```

</details>

<details>
<summary><strong>🔸 Deletar usuário</strong></summary>

```bash
curl -X DELETE http://localhost:8888/rest/v1/users/seuemail@teste.com
```

</details>

<details>
<summary><strong>🔸 Listar todos os usuários</strong></summary>

```bash
curl -X GET http://localhost:8888/rest/v1/users
```

</details>

---

## 🧪 Testes

O projeto separa **testes unitários** de **testes de integração** por convenção de nome:

| Tipo         | Sufixo       | Ferramenta Maven | Requer Docker |
| ------------ | ------------ | ---------------- | ------------- |
| Unitário     | `*Test.java` | Surefire         | Não           |
| Integração   | `*IT.java`   | Failsafe         | Sim (Testcontainers) |

Os testes de integração sobem um PostgreSQL efêmero via **Testcontainers**, aplicam as migrations do Flyway e batem no endpoint HTTP real (`TestRestTemplate` + porta aleatória).

| Comando         | Ação                                          |
| --------------- | --------------------------------------------- |
| `make test`     | Só testes unitários (rápido, sem Docker)      |
| `make test-it`  | Unit + integração (`mvn verify`, sobe container) |

---

## 📌 Observações

- Persistência em PostgreSQL 16 rodando via Docker (porta `55432`), com schema versionado pelo Flyway.
- Todas as regras de negócio estão isoladas da infraestrutura.
- O código é altamente testável e de fácil manutenção.

---

### 👨‍💻 Criado por

Desenvolvido por [@afralves](https://github.com/afralves)
