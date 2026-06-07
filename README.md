# 🚀 AresLife — API de Colonização Espacial

API REST para gerenciamento de colônias espaciais em Marte e na Lua, desenvolvida como projeto da Global Solution 2026 — FIAP.

---

## 🔗 Links do Projeto

|                               | Link |
|-------------------------------|------|
| 🌐 **Deploy (API pública)**   | https://areslife-java.onrender.com |
| 📖 **Swagger / Documentação** | https://areslife-java.onrender.com/swagger-ui.html |
| 💻 **Repositório GitHub**     | https://github.com/VitoriaMaglio/AresLife-Java |
| 🎥 **Vídeo API**              | *https://youtu.be/47iFv6tHRwQ* |
| 🎥 **Vídeo PITCH**            | *https://youtu.be/yQKSAJM5Xik* |

---

## 📋 Sobre o Projeto

O AresLife é uma plataforma de simulação e gestão de colonização espacial. O sistema permite:

- Cadastrar e monitorar **turistas espaciais** com destino a Marte ou à Lua
- Gerenciar **colônias** e seus **recursos vitais** (oxigênio, água, energia, alimentação)
- Monitorar a **saúde** dos habitantes em tempo real
- Emitir **alertas automáticos** quando recursos atingem nível crítico
- Controlar **treinamentos obrigatórios** e **viagens turísticas**
- Registrar toda operação em **logs de auditoria**

---

## 🏗️ Arquitetura e Tecnologias

| Tecnologia | Versão | Uso |
|-----------|--------|-----|
| Java | 21 | Linguagem principal |
| Spring Boot | 3.4.5 | Framework principal |
| Spring Data JPA | — | Persistência ORM |
| Spring Security | — | Autenticação e autorização |
| JWT (jjwt) | — | Tokens de acesso |
| Spring HATEOAS | — | Links hipermídia nas respostas |
| Spring Validation | — | Validação de dados de entrada |
| Lombok | — | Produtividade (builders, getters, etc.) |
| Spring Boot DevTools | — | Recarga automática em desenvolvimento |
| Springdoc OpenAPI | 2.x | Documentação Swagger |
| Oracle Database | 19c | Banco de dados relacional (FIAP) |
| ojdbc11 | — | Driver Oracle JDBC |
| Docker | — | Containerização para deploy |

---

## 🧩 Modelagem Avançada

| Requisito | Implementação |
|-----------|---------------|
| **Herança** | `Habitante` (base) → `Astronauta` e `Turista` com estratégia `SINGLE_TABLE` e discriminador `DTYPE` |
| **Chave composta** | `AvaliacaoTreinamentoId` com `@EmbeddedId` (habitante + treinamento) |
| **@Embedded** | `SinaisVitais` embutido dentro de `SaudeHabitante` |
| **Múltiplas tabelas** | 11 tabelas: colonias, habitantes, recursos, alertas, viagens_turisticas, treinamentos, saude_habitantes, monitoramento_recursos, logs_sistema, turistas_espaciais, usuarios |

---

## 🗂️ Estrutura do Projeto

```
src/main/java/com/fiap/areslife/
├── config/           # CorsConfig, SwaggerConfig, DataLoader
├── controller/       # Controllers REST com HATEOAS
├── dto/
│   ├── request/      # Records de entrada com @Valid
│   └── response/     # DTOs de saída
├── entity/           # Entidades JPA (herança, embedded, chave composta)
├── enums/            # Enumerações de domínio
├── exception/        # GlobalExceptionHandler + exceções customizadas
├── repository/       # JpaRepository
├── security/         # JwtFilter, SecurityConfig, CustomUserDetailsService
└── service/
    ├── mapper/       # Mappers entity → DTO
    └── *.java        # Services com regras de negócio
```

---

## ⚙️ Configuração de Credenciais

As credenciais do banco **não ficam no código**. O projeto usa **variáveis de ambiente**:

| Variável | Descrição | Exemplo |
|----------|-----------|---------|
| `DB_URL` | URL de conexão Oracle | `jdbc:oracle:thin:@oracle.fiap.com.br:1521:orcl` |
| `DB_USERNAME` | RM do aluno | `rm563509` |
| `DB_PASSWORD` | Senha do banco | `140607` |

### Rodando localmente

**Opção 1 — exportar no terminal antes de rodar:**
```bash
export DB_URL=jdbc:oracle:thin:@oracle.fiap.com.br:1521:orcl
export DB_USERNAME=seu_rm
export DB_PASSWORD=sua_senha
./mvnw spring-boot:run
```

**Opção 2 — criar arquivo `.env` na raiz** *(não é commitado, está no .gitignore)*:
```env
DB_URL=jdbc:oracle:thin:@oracle.fiap.com.br:1521:orcl
DB_USERNAME=seu_rm
DB_PASSWORD=sua_senha
```

> ⚠️ Cada desenvolvedor precisa configurar **suas próprias credenciais** do banco Oracle FIAP. O banco é individual por RM.

### No Render (deploy)

As variáveis são configuradas na aba **Environment** do Web Service, sem expor nada no código.

---

## ▶️ Como Executar Localmente

### Pré-requisitos
- Java 21+
- Maven 3.9+ (ou usar o `./mvnw` incluso)
- Credenciais do banco configuradas (ver seção acima)

### Passos

```bash
# 1. Clonar o repositório
git clone https://github.com/VitoriaMaglio/AresLife-Java.git
cd AresLife-Java

# 2. Configurar as variáveis de ambiente (ver seção acima)

# 3. Executar
./mvnw spring-boot:run
```

A API estará disponível em: `http://localhost:8080`  
Swagger: `http://localhost:8080/swagger-ui.html`

---

## 🔐 Autenticação JWT — Passo a Passo

A maioria dos endpoints exige autenticação via JWT. Os endpoints públicos (sem token) são:

| Endpoint | Descrição |
|----------|-----------|
| `POST /auth/login` | Obter token |
| `POST /auth/register` | Criar usuário |
| `GET /tourists` | Listar turistas (integração mobile) |
| `GET /tourists/{id}` | Buscar turista (integração mobile) |
| `POST /tourists` | Criar turista (integração mobile) |
| `PUT /tourists/{id}` | Atualizar turista (integração mobile) |
| `DELETE /tourists/{id}` | Deletar turista (integração mobile) |
| `GET /swagger-ui.html` | Documentação |
| `GET /v3/api-docs/**` | Spec OpenAPI |

### 1. Criar um usuário (primeiro acesso)

```http
POST /auth/register
Content-Type: application/json

{
  "email": "admin@areslife.com",
  "senha": "suasenha123"
}
```

A API gera automaticamente o hash BCrypt e salva no banco. **Nunca envie hash manualmente.**

### 2. Fazer login e obter o token

```http
POST /auth/login
Content-Type: application/json

{
  "email": "admin@areslife.com",
  "senha": "suasenha123"
}
```

Resposta:
```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9...",
  "tipo": "Bearer"
}
```

### 3. Usar o token nas requisições seguintes

Adicione o header em **todas** as requisições protegidas:

```
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9...
```

**No Postman:**
- Aba **Authorization** → Type: `Bearer Token`
- Cole o valor do campo `token`

**No Swagger:**
- Clique em **Authorize** (cadeado no topo)
- Digite: `Bearer eyJhbGciOiJIUzI1NiJ9...`

> O token expira em **24 horas**. Após isso, faça login novamente.

---

## 📡 Endpoints da API

### 🧑‍🚀 Turistas Espaciais — integração mobile (`/tourists`)

| Método | Endpoint | Autenticação | Descrição |
|--------|----------|:---:|-----------|
| GET | `/tourists` | ❌ | Listar todos |
| GET | `/tourists/{id}` | ❌ | Buscar por ID |
| POST | `/tourists` | ❌ | Criar turista |
| PUT | `/tourists/{id}` | ❌ | Atualizar turista |
| DELETE | `/tourists/{id}` | ❌ | Deletar turista |
| PATCH | `/tourists/{id}/status` | ✅ | Atualizar status |

**Modelo de entrada (POST/PUT):**
```json
{
  "name": "Laura Mendes",
  "age": 28,
  "nationality": "Brasil",
  "destination": "Marte"
}
```

**Modelo de saída:**
```json
{
  "id": "1",
  "name": "Laura Mendes",
  "age": 28,
  "nationality": "Brasil",
  "origin": "Terra",
  "destination": "Marte",
  "missionType": "Turismo espacial",
  "healthStatus": "Estável",
  "ticketStatus": "Pendente",
  "status": "Aguardando",
  "oxygenLevel": 95,
  "heartRate": 80,
  "missionDays": 3
}
```

---

### 🏙️ Colônias (`/api/colonias`) — requer token

| Método | Endpoint | Descrição |
|--------|----------|-----------|
| GET | `/api/colonias` | Listar todas |
| GET | `/api/colonias/{id}` | Buscar por ID |
| POST | `/api/colonias` | Criar colônia |
| PUT | `/api/colonias/{id}` | Atualizar colônia |
| DELETE | `/api/colonias/{id}` | Remover colônia |

---

### 🧪 Recursos (`/api/recursos`) — requer token

| Método | Endpoint | Descrição |
|--------|----------|-----------|
| GET | `/api/recursos/colonia/{coloniaId}` | Recursos de uma colônia |
| POST | `/api/recursos` | Criar recurso |
| POST | `/api/recursos/{id}/abastecer` | Abastecer recurso |
| POST | `/api/recursos/colonia/{id}/simular-dia` | Simular consumo diário |
| GET | `/api/recursos/{id}/autonomia` | Dias de autonomia |
| DELETE | `/api/recursos/{id}` | Remover recurso |

---

### 🚨 Alertas (`/api/alertas`) — requer token

| Método | Endpoint | Descrição |
|--------|----------|-----------|
| GET | `/api/alertas` | Listar (filtros: coloniaId, severidade, status) |
| GET | `/api/alertas/{id}` | Buscar por ID |
| PATCH | `/api/alertas/{id}/resolver` | Resolver alerta |

---

### 👨‍⚕️ Saúde (`/api/saude`) — requer token

| Método | Endpoint | Descrição |
|--------|----------|-----------|
| GET | `/api/saude/habitante/{id}` | Histórico de saúde |
| POST | `/api/saude/habitante/{id}` | Registrar sinais vitais |
| PUT | `/api/saude/{id}` | Atualizar registro |
| DELETE | `/api/saude/{id}` | Remover registro |

---

### 📋 Logs do Sistema (`/api/logs`) — requer token

Logs são gerados **automaticamente** a cada operação (INSERT, UPDATE, DELETE) em todos os services.

| Método | Endpoint | Descrição |
|--------|----------|-----------|
| GET | `/api/logs` | Listar todos os logs |
| GET | `/api/logs/tabela/{tabela}` | Filtrar por tabela |
| GET | `/api/logs/periodo` | Filtrar por período (`?inicio=...&fim=...`) |

**Tabelas disponíveis para filtro:**
`turistas_espaciais`, `colonias`, `recursos`, `alertas`, `habitantes`, `treinamentos`, `saude_habitantes`, `viagens_turisticas`

---

## 🧪 Testando com Postman

### Fluxo rápido para ver logs funcionando

```
1. POST /auth/register   → criar usuário
2. POST /auth/login      → obter token (copie o "token")
3. POST /tourists        → criar turista (sem token)
4. POST /api/colonias    → criar colônia (com token no header)
5. DELETE /tourists/1    → deletar turista (sem token)
6. GET /api/logs         → ver os logs gerados (com token)
```

### Configurar token no Postman de forma fácil

1. Crie uma **variável de ambiente** no Postman chamada `token`
2. Na requisição de login, vá em **Tests** e adicione:
```javascript
pm.environment.set("token", pm.response.json().token);
```
3. Em todas as outras requisições, na aba **Authorization** → Bearer Token → `{{token}}`

---

## 🚀 Deploy no Render

### Como foi feito

1. Repositório conectado ao Render via GitHub
2. **Dockerfile** na raiz do projeto (detalhes abaixo)
3. Variáveis de ambiente configuradas na aba **Environment** do Render

### Dockerfile

```dockerfile
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app
COPY ../../../AppData/Local/Temp/Rar$DRa7948.49372.rartemp .
RUN mvn clean package -DskipTests

FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /app/target/areslife-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
```

### Variáveis configuradas no Render

| Variável | Valor |
|----------|-------|
| `DB_URL` | `jdbc:oracle:thin:@oracle.fiap.com.br:1521:orcl` |
| `DB_USERNAME` | *(RM do aluno)* |
| `DB_PASSWORD` | *(senha do banco)* |
| `PORT` | `8080` |

> ⚠️ O plano gratuito do Render entra em modo de espera após 15 minutos sem uso. A primeira requisição pode demorar ~30s para acordar.

---

## 👥 Integrantes

| Nome                     | RM       |
|--------------------------|----------|
| Vitoria Maglio           | RM563509 |
| Felipe Maglio            | RM563512 |
| Mateus Granja dos Santos | RM564930 |
| Mariana Magalhães        | RM561786 |
| João Pedro Bitencourt    | RM564339 |
---

## 📁 Arquivos Extras

| Arquivo | Descrição |
|---------|-----------|
| `AresLife API.postman_collection.json` | Coleção completa do Postman para testar todos os endpoints |
| `Dockerfile` | Configuração Docker para deploy |

