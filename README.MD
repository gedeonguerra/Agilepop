# AgilePop API

![CI](https://github.com/gedeonguerra/Agilepop/actions/workflows/ci.yml/badge.svg)

API para gerenciamento e divulgação de eventos da plataforma AgilePop.

---

## 📌 Sobre o Projeto

O **AgilePop API** é uma aplicação REST desenvolvida com **Java 17 + Spring Boot**, voltada para o cadastro, listagem, atualização e remoção de eventos.  
Idealizada de forma **colaborativa** e **voluntária** pelo grupo **Pipoca Ágil**, é um projeto de código aberto com foco educacional e social.

O projeto conta com **integração contínua (CI)** via GitHub Actions, rodando a cada push: build, testes unitários e de integração, geração de relatório de cobertura (JaCoCo) e testes de API automatizados (Postman/Newman).

---

## 🚀 Como rodar o projeto localmente

Você pode rodar a aplicação de duas formas:

- [x] **Usando Maven localmente**
- [x] **Usando Docker Compose (PostgreSQL via container)**

---

### 🧪 Opção 1: Rodando com Maven (modo local)

#### ✅ Pré-requisitos

- Java 17 (ou superior)  
- Maven 3.6+ instalado  
- Git instalado  

#### ▶️ Passo a passo

1. **Clone o repositório**

    ```bash
    git clone https://github.com/gedeonguerra/Agilepop.git
    cd Agilepop
    ```

2. **Compile e instale dependências**

    ```bash
    mvn clean install
    ```

3. **Execute a aplicação**

    ```bash
    mvn spring-boot:run
    ```

4. **A API estará rodando em:**

   ```bash
   http://localhost:8080/api/eventos
   ```

5. **Acesse a documentação Swagger para testar endpoints:**

   ```bash
   http://localhost:8080/swagger-ui/index.html
   ```

---

### 🐳 Opção 2: Rodando com Docker Compose

#### *✅ Pré-requisitos

- Docker instalado e funcionando  
- Docker Desktop no Windows/macOS  

#### *▶️ Passo a passo

1. **Clone o repositório**

    ```bash
    git clone https://github.com/gedeonguerra/Agilepop.git
    cd Agilepop
    ```

2. **Suba o container com PostgreSQL**

    ```bash
    docker-compose up -d
    ```

3. **Verifique se o banco está ativo**

    O banco será iniciado no container com as seguintes credenciais:

    - Banco: `agilepop`  
    - Usuário: `postgres`  
    - Senha: `123456`  

    A aplicação usa o perfil `application-prod.properties`, que já está configurado para conectar nesse banco.

4. **Acesse a documentação Swagger:**

    ```bash
    http://localhost:8080/swagger-ui/index.html
    ```

---

❗ **Importante**:  
O arquivo `application.properties` está comentado propositalmente para que o `application-prod.properties` seja utilizado com o banco PostgreSQL.

💡 **Dica**: Se quiser parar os containers:

   ```bash
   docker-compose down
   ```

## 🧪 Testes automatizados

### Testes unitários e de integração (JUnit + MockMvc)

O projeto cobre os 5 endpoints do `EventoController` (criar, listar, buscar por ID, atualizar e deletar), incluindo cenários de sucesso, validação e erro (404).

Para rodar:

```bash
mvn test
```

### Cobertura de código (JaCoCo)

O relatório de cobertura é gerado automaticamente a cada execução dos testes:

```bash
mvn clean verify
```

O relatório em HTML fica disponível em:

```
target/site/jacoco/index.html
```

### Testes de API (Postman/Newman)

A collection `postman/AgilePopApi.postman_collection.json` cobre o fluxo completo dos 5 endpoints (criar → listar → buscar → atualizar → deletar), validando status code e corpo da resposta.

Para rodar localmente (com a aplicação já em execução):

```bash
npm install -g newman
newman run postman/AgilePopApi.postman_collection.json
```

### Integração Contínua (CI)

Todo push ou pull request para a branch `main` dispara o workflow `.github/workflows/ci.yml`, que executa automaticamente:

1. Build + testes unitários/integração + relatório JaCoCo
2. Empacotamento da aplicação
3. Subida da aplicação e execução da collection Postman via Newman

Veja o histórico de execuções na aba [Actions](https://github.com/gedeonguerra/Agilepop/actions) do repositório.

---

## 👩‍💻 Para o time de QA — Testes manuais

- Use o Swagger UI para testar os endpoints HTTP (GET, POST, PUT, DELETE).
- Teste os seguintes casos:

  - Criar evento com dados válidos (deve retornar status 201 Created)
  - Criar evento sem título (deve retornar 400 Bad Request com mensagem de erro)
  - Criar evento com data no passado (deve retornar 400)
  - Criar evento com descrição muito longa (deve retornar 400)
  - Listar eventos (deve retornar status 200 OK e lista de eventos)
  - Buscar evento por ID existente (200 OK) e inexistente (404 Not Found)
  - Atualizar evento existente (200 OK) e com dados inválidos (400 Bad Request)
  - Deletar evento existente (204 No Content) e inexistente (404 Not Found)

- Utilize ferramentas como [Postman](https://www.postman.com/downloads/) ou Insomnia para testar endpoints também.

---

## 📁 Estrutura do projeto

- `com.agilepop.model` — Entidade Evento
- `com.agilepop.controller` — Controllers REST (Evento, Upload)
- `com.agilepop.repository` — Interface JPA Repository
- `com.agilepop.exception` — Tratamento global de erros
- `com.agilepop.config` — Configurações de Segurança e OpenAPI/Swagger
- `com.agilepop` (em `src/test`) — Testes com JUnit e MockMvc
- `postman/` — Collection de testes de API (Postman/Newman)
- `.github/workflows/` — Pipeline de CI (GitHub Actions)

---

## 👥 Equipe

Este repositório contém o backend do projeto AgilePop, desenvolvido de forma colaborativa e voluntária pelo grupo **Pipoca Ágil**.

Testes automatizados, cobertura de código (JaCoCo), pipeline de CI/CD e testes de API (Postman/Newman): **Neto Guerra** (QA Tester)

Veja a equipe completa no [repositório principal](https://github.com/gedeonguerra/AgilePopcorn).

---

## 📞 Contato

Para dúvidas ou contribuições, entre em contato:  
Victor - Desenvolvedor  
Email: [contato@agilepop.com](mailto:contato@agilepop.com)  
GitHub: [https://github.com/victor](https://github.com/victor)

---

## 📜 Licença

Projeto aberto sob licença Apache 2.0.  
Veja detalhes em: [https://www.apache.org/licenses/LICENSE-2.0.html](https://www.apache.org/licenses/LICENSE-2.0.html)