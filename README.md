# REST API — Clean Architecture com Spring Boot

![Java](https://img.shields.io/badge/Java-21-orange?logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.3-brightgreen?logo=springboot&logoColor=white)
![REST](https://img.shields.io/badge/API-REST-red?logo=fastapi&logoColor=white)
![Maven](https://img.shields.io/badge/Maven-Build-blue?logo=apachemaven&logoColor=white)
![H2](https://img.shields.io/badge/H2-Database-lightblue?logo=h2&logoColor=white)

> **REST API** implementada seguindo os princípios de **Clean Architecture**, utilizando **Spring Boot 3.5.3**, com persistência em **H2 Database** e organização por camadas de domínio, aplicação e infraestrutura.

---

## 📦 Tecnologias Utilizadas

| Tecnologia          | Detalhe        |
| ------------------- | -------------- |
| **Java**            | 21             |
| **Spring Boot**     | 3.5.3          |
| **Spring Web**      | —              |
| **Spring Data JPA** | —              |
| **H2 Database**     | (em memória)   |
| **Maven**           | —              |
| **Lombok**          | —              |

---

## 🧱 Estrutura do Projeto

O projeto segue os princípios da **Clean Architecture**, separando responsabilidades por camadas:

```text
com.afralves.cleanarchitecture
├── domain
│   └── entity, gateway, exception
├── application
│   └── usecases
├── infrastructure
│   └── adapter
│       ├── controller
│       ├── persistence
│       │   ├── repository
│       │   ├── model
│       │   └── converter
├── main
│   └── config (beans, config do app)
```

---

## 🚀 Como executar

**1. Clone o repositório:**

```bash
git clone https://github.com/seu-usuario/cleanarchitecture.git
cd cleanarchitecture
```

**2. Compile e rode o projeto:**

```bash
./mvnw spring-boot:run
```

**3. Acesse a aplicação:**

| Recurso        | URL                                    |
| -------------- | -------------------------------------- |
| 🌐 **API**      | `http://localhost:8888`                |
| 🗄️ **H2 Console** | `http://localhost:8888/h2-console`  |

**Credenciais do H2 Console:**

| Campo        | Valor                    |
| ------------ | ------------------------ |
| **JDBC URL** | `jdbc:h2:mem:testdb`     |
| **User**     | `sa`                     |
| **Password** | *(deixe em branco)*      |

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

## 📌 Observações

- O projeto está preparado para evoluir facilmente para um banco relacional real (como PostgreSQL).
- Todas as regras de negócio estão isoladas da infraestrutura.
- O código é altamente testável e de fácil manutenção.

---

### 👨‍💻 Criado por

Desenvolvido por [@afralves](https://github.com/afralves)
