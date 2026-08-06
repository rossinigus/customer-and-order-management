# Customer and Order Management

API REST para gerenciamento de clientes e pedidos, desenvolvida com Spring Boot. Permite cadastrar, consultar, atualizar e remover clientes e pedidos, aplicando regras de negócio como CPF único, validação de e-mail e valor de pedido positivo.

## Tecnologias utilizadas

- Java 17
- Spring Boot 4.1
- Spring Web
- Spring Data JPA
- Banco de dados H2 (em memória)
- Maven
- Git

## Regras de negócio

### Cliente (Customer)

| Campo | Regra |
|---|---|
| Nome | Obrigatório |
| CPF | Obrigatório, 11 dígitos numéricos, não pode ser duplicado |
| Email | Obrigatório, deve possuir formato válido |

### Pedido (Order)

| Campo | Regra |
|---|---|
| Descrição | Obrigatória |
| Valor | Obrigatório, deve ser maior que zero |
| Cliente | Obrigatório, precisa existir previamente cadastrado |

Um cliente pode possuir vários pedidos (relacionamento um-para-muitos).

## Como executar o projeto

### Pré-requisitos

- Java 17 ou superior instalado
- Não é necessário ter Maven instalado — o projeto usa o Maven Wrapper (`mvnw`)

### Passos

1. Clone o repositório:
   ```bash
   git clone https://github.com/SEU_USUARIO/customer-and-order-management.git
   cd customer-and-order-management
   ```

2. Execute a aplicação:

   No Windows (PowerShell):
   ```powershell
   .\mvnw spring-boot:run
   ```

   No Linux/Mac ou Git Bash:
   ```bash
   ./mvnw spring-boot:run
   ```

3. A aplicação sobe por padrão na porta `8080`: `http://localhost:8080`

### Acessando o console do banco H2

Com a aplicação rodando, acesse `http://localhost:8080/h2-console` no navegador e conecte usando:

- **JDBC URL:** `jdbc:h2:mem:testdb`
- **User Name:** `sa`
- **Password:** (a senha configurada em `application.properties`)

## Endpoints disponíveis

### Clientes

| Método | Endpoint | Descrição |
|---|---|---|
| POST | `/customers` | Cadastra um novo cliente |
| GET | `/customers` | Lista todos os clientes |
| GET | `/customers/{id}` | Busca um cliente pelo ID |
| PUT | `/customers/{id}` | Atualiza um cliente existente |
| DELETE | `/customers/{id}` | Remove um cliente |

**Exemplo de corpo (POST/PUT):**
```json
{
  "name": "João Silva",
  "cpf": "12345678900",
  "email": "joao@email.com"
}
```

### Pedidos

| Método | Endpoint | Descrição |
|---|---|---|
| POST | `/orders` | Cadastra um novo pedido |
| GET | `/orders` | Lista todos os pedidos |
| GET | `/orders/{id}` | Busca um pedido pelo ID |
| PUT | `/orders/{id}` | Atualiza um pedido existente |
| DELETE | `/orders/{id}` | Remove um pedido |

**Exemplo de corpo (POST/PUT):**
```json
{
  "description": "Notebook Dell",
  "value": 3500.00,
  "customer": {
    "id": 1
  }
}
```

> O campo `customer` exige apenas o `id` de um cliente já cadastrado.

## Tratamento de erros

Erros de validação e regras de negócio (CPF duplicado, cliente não encontrado, valor inválido, etc.) retornam status `400 Bad Request` com um corpo padronizado:

```json
{
  "timestamp": "2026-08-06T10:15:30",
  "status": 400,
  "message": "CPF already registered."
}
```

## Estrutura do projeto

```
src/main/java/com/example/customer_and_order_management/
├── controller/     # Endpoints REST
├── service/        # Regras de negócio e validações
├── repository/     # Acesso a dados (Spring Data JPA)
├── model/          # Entidades (Customer, Order)
└── exception/       # Tratamento centralizado de erros
```
