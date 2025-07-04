# Clean Architecture - Spring Boot

Este projeto é uma implementação de **Clean Architecture** utilizando **Spring Boot 3.5.3**, com persistência em **H2 Database** e organização por camadas de domínio, aplicação e infraestrutura.

## 📦 Tecnologias Utilizadas

- Java 21
- Spring Boot 3.5.3
- Spring Web
- Spring Data JPA
- H2 Database (em memória)
- Maven
- Lombok

## 🧱 Estrutura do Projeto

O projeto segue os princípios da **Clean Architecture**, separando responsabilidades por camadas:

```
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

## 🚀 Como executar

1. Clone o repositório:

```bash
git clone https://github.com/seu-usuario/cleanarchitecture.git
cd cleanarchitecture
```

2. Compile e rode o projeto:

```bash
./mvnw spring-boot:run
```

3. Acesse a aplicação:
- API: `http://localhost:8080`
- H2 Console: `http://localhost:8080/h2-console`
  - JDBC URL: `jdbc:h2:mem:testdb`
  - User: `sa`
  - Password: *(deixe em branco)*

## 💻 Exemplo de uso da API via `curl`

#### 🔸 Criar usuário

```bash
curl -X POST http://localhost:8080/rest/v1/users \
  -H "Content-Type: application/json" \
  -d '{
    "email": "exemplo@teste.com",
    "password": "123456",
    "name": "João da Silva"
  }'
```

#### 🔸 Atualizar senha do usuário

```bash
curl -X PUT http://localhost:8080/rest/v1/users \
  -H "Content-Type: application/json" \
  -d '{
    "email": "exemplo@teste.com",
    "password": "novaSenha123"
  }'
```

#### 🔸 Deletar usuário

```bash
curl -X DELETE http://localhost:8080/rest/v1/users/seuemail@teste.com
```

#### 🔸 Listar todos os usuários

```bash
curl -X GET http://localhost:8080/rest/v1/users
```

## 📌 Observações

- O projeto está preparado para evoluir facilmente para um banco relacional real (como PostgreSQL).
- Todas as regras de negócio estão isoladas da infraestrutura.
- O código é altamente testável e de fácil manutenção.

### 👨‍💻 Criado por

Desenvolvido por [@afralves](https://github.com/afralves)
