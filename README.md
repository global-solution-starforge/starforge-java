<div align="center">

# StarForge - Backend API

**Plataforma de financiamento coletivo para missões espaciais**

[![Java](https://img.shields.io/badge/Java-21-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)](https://openjdk.org/projects/jdk/21/)
[![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.4.5-6DB33F?style=for-the-badge&logo=spring&logoColor=white)](https://spring.io/projects/spring-boot)
[![Oracle](https://img.shields.io/badge/Oracle_DB-F80000?style=for-the-badge&logo=oracle&logoColor=white)](https://www.oracle.com/database/)
[![Swagger](https://img.shields.io/badge/Swagger-UI-85EA2D?style=for-the-badge&logo=swagger&logoColor=black)](https://swagger.io/)
[![Docker](https://img.shields.io/badge/Docker-2496ED?style=for-the-badge&logo=docker&logoColor=white)](https://www.docker.com/)

</div>

---

## Sumário

- [Sobre o Projeto](#sobre-o-projeto)
- [Links do Projeto](#links-do-projeto)
- [Tecnologias Utilizadas](#tecnologias-utilizadas)
- [Arquitetura](#arquitetura)
- [Documentação da API](#documentação-da-api)
- [Instruções de Execução](#instruções-de-execução)
- [Variáveis de Ambiente](#variáveis-de-ambiente)
- [Segurança e Autenticação](#segurança-e-autenticação)
- [Integrantes](#integrantes)

---

## Sobre o Projeto

StarForge é uma plataforma de financiamento coletivo voltada para missões de exploração espacial. Pilotos podem se cadastrar, contribuir financeiramente para missões ativas, acompanhar o progresso de cada fase e desbloquear naves exclusivas para seu hangar pessoal com base nas contribuições realizadas.

A API gerencia o ciclo completo da plataforma: desde o alistamento de pilotos e criação de missões por agências espaciais, até o controle de contribuições por tiers, desbloqueio de naves e visualização do progresso de financiamento em tempo real.

---

## Links do Projeto

| Recurso | Link |
|---|---|
| Deploy (API) | `https://starforge-java.onrender.com/` |
| Vídeo de Apresentação | `Em breve` |
| Documentação Swagger (local) | `http://localhost:8080/swagger-ui.html` |
| API Docs JSON (local) | `http://localhost:8080/api-docs` |

---

## Tecnologias Utilizadas

| Tecnologia | Versão | Finalidade |
|---|---|---|
| Java | 21 | Linguagem principal |
| Spring Boot | 3.4.5 | Framework base |
| Spring Security | — | Autenticação e autorização |
| Spring Data JPA | — | Persistência de dados (ORM) |
| Spring HATEOAS | — | Respostas hypermedia |
| Spring Validation | — | Validação de entradas |
| Auth0 JWT | 4.4.0 | Geração e validação de tokens JWT |
| SpringDoc OpenAPI | 2.8.9 | Documentação interativa (Swagger UI) |
| Oracle Database | 11+ | Banco de dados relacional |
| Lombok | — | Redução de boilerplate |
| Docker | — | Containerização e deploy |
| Gradle | — | Gerenciamento de build e dependências |

---

## Arquitetura

O projeto segue uma arquitetura em camadas, organizada por responsabilidade:

```
src/main/java/com/starforge/backend_server/
├── config/           # Configurações globais (PasswordEncoder, etc.)
├── controller/       # Camada REST — 9 controllers
├── database/
│   ├── model/        # Entidades JPA (20+ classes + enums)
│   └── repository/   # Interfaces Spring Data JPA
├── dto/              # Data Transfer Objects por domínio
├── exception/        # Exceções customizadas
├── handler/          # Tratamento global de erros
├── security/         # JWT filter, SecurityConfig, TokenService
└── service/          # Regras de negócio — 10 services
```

### Diagrama de Entidades Principais

```
Agencia / Organizacao
       └── Missao ──── FaseMissao
                  └── Nave
                        │
               Tier ───┤
                  └── Contribuicao ──── Hangar
                              │
                           Usuario
```

---

## Documentação da API

A documentação completa e interativa está disponível via **Swagger UI** ao executar a aplicação.

**URL local:** `http://localhost:8080/swagger-ui.html`

### Visão Geral dos Endpoints

Todos os endpoints utilizam o prefixo `/v1/`.

---

#### Autenticação — `/v1/auth`

| Método | Endpoint | Descrição | Auth |
|---|---|---|---|
| `POST` | `/v1/auth/register` | Cadastro de novo piloto (Alistamento Orbital) | Público |
| `POST` | `/v1/auth/login` | Login — retorna JWT + ID do usuário | Público |

---

#### Usuários — `/v1/usuarios`

| Método | Endpoint | Descrição | Auth |
|---|---|---|---|
| `GET` | `/v1/usuarios/` | Listar todos os pilotos | ADMIN |
| `GET` | `/v1/usuarios/{id}` | Buscar piloto por ID | Autenticado |
| `GET` | `/v1/usuarios/{id}/resumo` | Resumo: total de contribuições e missões apoiadas | Autenticado |
| `PUT` | `/v1/usuarios/{id}` | Atualizar dados do piloto | Autenticado |
| `DELETE` | `/v1/usuarios/{id}` | Desativar conta (soft delete) | Autenticado |

---

#### Missões — `/v1/missoes`

| Método | Endpoint | Descrição | Auth |
|---|---|---|---|
| `GET` | `/v1/missoes/` | Listar todas as missões | Público |
| `GET` | `/v1/missoes/ativas` | Listar missões ativas | Público |
| `GET` | `/v1/missoes/{id}` | Buscar missão por ID | Público |
| `GET` | `/v1/missoes/{id}/progresso` | Progresso de financiamento da missão | Público |
| `GET` | `/v1/missoes/{id}/fases` | Listar fases da missão | Público |
| `POST` | `/v1/missoes/` | Criar nova missão | ADMIN |
| `PUT` | `/v1/missoes/{id}` | Atualizar missão | ADMIN |
| `PUT` | `/v1/missoes/{id}/fases/{numeroFase}` | Atualizar fase específica | ADMIN |
| `DELETE` | `/v1/missoes/{id}` | Remover missão | ADMIN |

---

#### Naves — `/v1/naves`

| Método | Endpoint | Descrição | Auth |
|---|---|---|---|
| `GET` | `/v1/naves/` | Listar todas as naves | Público |
| `GET` | `/v1/naves/missao/{missaoId}` | Buscar nave por missão | Público |
| `POST` | `/v1/naves/` | Cadastrar nave | ADMIN |
| `PUT` | `/v1/naves/{id}` | Atualizar nave | ADMIN |
| `DELETE` | `/v1/naves/{id}` | Remover nave | ADMIN |

---

#### Contribuições — `/v1/contribuicoes`

| Método | Endpoint | Descrição | Auth |
|---|---|---|---|
| `POST` | `/v1/contribuicoes/` | Criar contribuição (gera entrada no hangar automaticamente) | Autenticado |
| `GET` | `/v1/contribuicoes/usuario/{id}` | Listar contribuições do piloto | Autenticado |
| `GET` | `/v1/contribuicoes/missao/{id}` | Listar contribuições de uma missão | ADMIN |
| `PUT` | `/v1/contribuicoes/{id}/status` | Atualizar status da contribuição | ADMIN |

---

#### Hangar — `/v1/hangar`

| Método | Endpoint | Descrição | Auth |
|---|---|---|---|
| `GET` | `/v1/hangar/usuario/{id}` | Listar naves do hangar do piloto | Autenticado |
| `POST` | `/v1/hangar/desbloquear` | Desbloquear nave no hangar | Autenticado |
| `DELETE` | `/v1/hangar/{id}` | Remover entrada do hangar | Autenticado |

---

#### Tiers — `/v1/tiers`

| Método | Endpoint | Descrição | Auth |
|---|---|---|---|
| `GET` | `/v1/tiers/` | Listar tiers disponíveis | Público |
| `GET` | `/v1/tiers/{id}` | Buscar tier por ID | Público |
| `POST` | `/v1/tiers/` | Criar tier | ADMIN |
| `PUT` | `/v1/tiers/{id}` | Atualizar tier | ADMIN |
| `DELETE` | `/v1/tiers/{id}` | Remover tier | ADMIN |

---

#### Agências — `/v1/agencias`

| Método | Endpoint | Descrição | Auth |
|---|---|---|---|
| `GET` | `/v1/agencias/` | Listar agências | Público |
| `GET` | `/v1/agencias/{id}` | Buscar agência por ID | Público |
| `POST` | `/v1/agencias/` | Criar agência | ADMIN |
| `PUT` | `/v1/agencias/{id}` | Atualizar agência | ADMIN |
| `DELETE` | `/v1/agencias/{id}` | Remover agência | ADMIN |

---

#### Organizações — `/v1/organizacoes`

| Método | Endpoint | Descrição | Auth |
|---|---|---|---|
| `GET` | `/v1/organizacoes/` | Listar organizações | Público |
| `GET` | `/v1/organizacoes/{id}` | Buscar organização por ID | Público |
| `POST` | `/v1/organizacoes/` | Criar organização | ADMIN |
| `PUT` | `/v1/organizacoes/{id}` | Atualizar organização | ADMIN |
| `DELETE` | `/v1/organizacoes/{id}` | Remover organização | ADMIN |

---

## Instruções de Execução

### Pré-requisitos

- Java 21+
- Gradle 8+
- Docker (opcional, para execução containerizada)
- Acesso a um banco de dados Oracle

---

### Opção 1 — Executar com Gradle (local)

**1. Clone o repositório:**
```bash
git clone https://github.com/global-solution-starforge/starforge-java.git
cd starforge-java
```

**2. Configure as variáveis de ambiente** (ver seção [Variáveis de Ambiente](#variáveis-de-ambiente)).

**3. Execute a aplicação:**
```bash
./gradlew bootRun
```

A API estará disponível em `http://localhost:8080`.

---

### Opção 2 — Executar com Docker

**1. Construir a imagem:**
```bash
docker build -t starforge-api .
```

**2. Executar o container:**
```bash
docker run -p 8080:8080 \
  -e DATASOURCE_URL=jdbc:oracle:thin:@//HOST:1521/SERVICE \
  -e DATASOURCE_USERNAME=seu_usuario \
  -e DATASOURCE_PASSWORD=sua_senha \
  -e JWT_SECRET=sua_chave_secreta \
  starforge-api
```

A API estará disponível em `http://localhost:8080`.

---

### Verificar o funcionamento

Após iniciar a aplicação, acesse:

- **Swagger UI:** `http://localhost:8080/swagger-ui.html`
- **Health check (teste rápido):** `GET http://localhost:8080/v1/missoes/`

---

## Variáveis de Ambiente

| Variável | Descrição | Padrão (desenvolvimento) |
|---|---|---|
| `DATASOURCE_URL` | URL JDBC do banco de dados Oracle | `jdbc:oracle:thin:@//oracle.fiap.com.br:1521/orcl` |
| `DATASOURCE_USERNAME` | Usuário do banco de dados | `rm561928` |
| `DATASOURCE_PASSWORD` | Senha do banco de dados | — |
| `JWT_SECRET` | Chave secreta para assinar tokens JWT | `starforge-jwt-secret-key` |
| `PORT` | Porta em que a API vai subir | `8080` |

> **Atenção:** Nunca exponha credenciais em repositórios públicos. Utilize variáveis de ambiente ou ferramentas de secrets management em produção.

---

## Segurança e Autenticação

A API utiliza autenticação baseada em **JWT (JSON Web Token)** com o algoritmo **HMAC256**.

### Fluxo de autenticação

```
1. POST /v1/auth/login  →  { "email": "...", "senha": "..." }
2. Resposta: { "token": "eyJ...", "id": 1 }
3. Requisições subsequentes: Header  →  Authorization: Bearer eyJ...
```

### Propriedades do token

| Propriedade | Valor |
|---|---|
| Algoritmo | HMAC256 |
| Issuer | `starforge` |
| Expiração | 24 horas |

### Níveis de acesso

| Nível | Descrição |
|---|---|
| **Público** | Sem autenticação — leitura de missões, naves, tiers, agências e organizações |
| **Autenticado** | Requer JWT válido — operações do próprio piloto (hangar, contribuições, perfil) |
| **ADMIN** | Requer JWT + role ADMIN — criação, edição e exclusão de recursos |

---

## Informações Relevantes para Avaliação

### Funcionalidades implementadas

- **CRUD completo** para todas as entidades: Missões, Naves, Tiers, Agências, Organizações, Usuários
- **Autenticação JWT** com controle de acesso por roles (ADMIN / USER)
- **Sistema de contribuições** com múltiplos métodos de pagamento (PIX, Cartão, Boleto, Transferência)
- **Hangar de naves** desbloqueáveis automaticamente via contribuição
- **Fases de missão** com controle de status individualizado
- **HATEOAS** — todas as respostas incluem links de navegação entre recursos relacionados
- **Documentação OpenAPI** — Swagger UI disponível em `/swagger-ui.html`
- **Soft delete** em usuários (desativação sem exclusão física)
- **Deploy containerizado** via Docker com configuração para Render (`render.yaml`)

### Padrões e boas práticas

- Separação em camadas: Controller → Service → Repository
- DTOs para todas as entradas e saídas da API
- Tratamento global de exceções com respostas padronizadas
- Validação de entrada com Bean Validation (`@Valid`)
- Senhas armazenadas com hash BCrypt
- CORS habilitado para integração com frontends externos

---

## Integrantes

| Nome | RM |
|---|---|
| Anna Clara Russo Luca | RM561928 |
| Gabriel Duarte Maciel | RM565754 |
| Gustavo Tavares da Silva | RM562827 |
| Tiago Guedes da Costa | RM564731 |

---

<div align="center">

**FIAP — Global Solution 2026 · 3º Semestre**

</div>
