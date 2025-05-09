# 💰 FinanceControl

Sistema de controle financeiro pessoal com autenticação JWT, cadastro de receitas e despesas, e visualização de transações.

## 🛠️ Tecnologias

- Java 21
- Spring Boot
- Spring Security
- JWT (Auth0)
- Flyway
- PostgreSQL
- JPA / Hibernate
- Lombok
- JUnit 5 + Mockito

## 🚀 Funcionalidades

- ✅ Cadastro e login de usuários
- ✅ Autenticação com JWT
- ✅ CRUD de transações financeiras
- ✅ Filtros por receita (revenue) e despesa (expense)
- ✅ Validações e tratamento global de erros
- ✅ Testes unitários com JUnit e Mockito

# 📌 Endpoints da API

## 🔐 Autenticação

| Método | Endpoint         | Descrição                            |
|--------|------------------|----------------------------------------|
| POST   | `/auth/register` | Registra um novo usuário               |
| POST   | `/auth/login`    | Autentica o usuário e retorna um token JWT |

## 💸 Transações

| Método | Endpoint                | Descrição                                             |
|--------|-------------------------|-------------------------------------------------------|
| GET    | `/transactions`         | Lista todas as transações do usuário logado           |
| GET    | `/transactions/{id}`    | Retorna os detalhes de uma transação específica       |
 GET    | `/transactions/saldo`   | Retorna o saldo de um usuário                         |
 GET    | `/transactions/filter`  | Retorna os dados de uma transações utilizando filtros |
| POST   | `/transactions/revenue` | Cria uma nova transação do tipo receita               |
| POST   | `/transactions/expense` | Cria uma nova transação do tipo despesa               |
| PUT    | `/transactions/{id}`    | Atualiza completamente uma transação                  |
| PATCH  | `/transactions/{id}`    | Atualiza parcialmente uma transação                   |
| DELETE | `/transactions/{id}`    | Remove uma transação existente                        |


## 🔐 Autenticação

A autenticação é feita via JWT. Após o login, o token deve ser enviado no header das requisições autenticadas:

```http
Authorization: Bearer <token>
```

## 🧪 Testes

Para rodar os testes unitários:

```bash
./mvnw test
```

## 📋 Pré-requisitos

- Java 17
- Maven 3.8+
- PostgreSQL rodando na porta padrão (5432)

## ⚙️ Configuração

Configure o `application.properties`:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/financecontrol
spring.datasource.username=seu_usuario
spring.datasource.password=sua_senha
jwt.secret=segredo_super_secreto
```

Rodar as migrações do Flyway:

```bash
./mvnw flyway:migrate
```

## ▶️ Executando o projeto

```bash
./mvnw spring-boot:run
```

## 📫 Contato

Desenvolvido por [Murilo Dorigo](https://github.com/dorigodev) 💻