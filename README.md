````md
# 📚 API REST - Gerenciamento de Cursos

Projeto desenvolvido com Java + Spring Boot para gerenciamento de cursos de uma instituição de ensino seguindo arquitetura REST.

---

# 🚀 Tecnologias Utilizadas

- Java 21
- Spring Boot 3.x
- Spring Web
- Spring Data JPA
- Flyway
- PostgreSQL
- Maven
- Swagger / OpenAPI
- Lombok

---

# ⚙️ Funcionalidades

✅ Cadastro de cursos
✅ Atualização de cursos
✅ Exclusão lógica (`ativo = false`)
✅ Listagem apenas de cursos ativos
✅ Busca por ID
✅ Listagem dos períodos disponíveis
✅ Validação de dados
✅ Tratamento de erros
✅ Documentação Swagger
✅ Versionamento do banco com Flyway

---

# 🧠 Regras de Negócio

Cada curso possui:

| Campo   | Regra                                                            |
| ------- | ---------------------------------------------------------------- |
| id      | Gerado automaticamente                                           |
| nome    | Obrigatório, único e mínimo 3 caracteres                         |
| periodo | Obrigatório e deve ser MATUTINO, VESPERTINO, NOTURNO ou INTEGRAL |
| ativo   | Exclusão lógica                                                  |

---

# 🗂️ Enum de Períodos

```java
public enum Periodo {
    MATUTINO,
    VESPERTINO,
    NOTURNO,
    INTEGRAL
}
```

---

# 🔥 Endpoints da API

## 📌 Cursos

| Método | Endpoint         | Descrição               |
| ------ | ---------------- | ----------------------- |
| GET    | /cursos          | Lista cursos ativos     |
| GET    | /cursos/{id}     | Busca curso por ID      |
| POST   | /cursos          | Cadastra um curso       |
| PUT    | /cursos          | Atualiza um curso       |
| DELETE | /cursos/{id}     | Exclusão lógica         |
| GET    | /cursos/periodos | Lista todos os períodos |

---

# 📦 Exemplos de Requisição

## ➕ POST `/cursos`

```json
{
  "nome": "Análise e Desenvolvimento de Sistemas",
  "periodo": "NOTURNO"
}
```

---

## ✏️ PUT `/cursos`

```json
{
  "id": 1,
  "nome": "Ciência da Computação",
  "periodo": "INTEGRAL"
}
```

---

# ✅ Códigos de Retorno

| Status | Significado |
| ------ | ----------- |
| 200    | OK          |
| 201    | Created     |
| 204    | No Content  |
| 404    | Not Found   |
| 409    | Conflict    |

---

# 🛢️ Banco de Dados

O projeto utiliza:

* PostgreSQL
* Flyway para versionamento

A migration inicial cria a tabela `cursos` com todas as restrições necessárias.

---

# ▶️ Como Rodar o Projeto

## 1️⃣ Clone o repositório

```bash
git clone https://github.com/Isabella751/cadastroDeCursoBKD.git
```

---

## 2️⃣ Configure o banco PostgreSQL

Crie um banco chamado:

```sql
CREATE DATABASE cadastro_de_curso_bkd;
```

---

## 3️⃣ Configure o `application.properties`

```properties
spring.application.name=cadastroDeCursoBKD
spring.datasource.url=jdbc:mariadb://localhost:3306/cadastro_de_curso_bkd
spring.datasource.username=root
spring.datasource.password=senai2026
springdoc.swagger-ui.path=teste
```

---

## 4️⃣ Execute o projeto

Pelo terminal:

```bash
./mvnw spring-boot:run
```

Ou execute a classe principal pela IDE.

---

# 📖 Swagger

Após iniciar a aplicação:

```bash
http://localhost:8080/swagger-ui.html
```

ou

```bash
http://localhost:8080/swagger-ui/index.html
```

---

# 🧪 Exemplo de Resposta

```json
{
  "id": 1,
  "nome": "Ciência da Computação",
  "periodo": "INTEGRAL"
}
```

---

# 📌 Autor

Isabella Leite dos Santos

```
