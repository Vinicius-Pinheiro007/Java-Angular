# SAT – Java/Angular – Pedidos

Projeto fullstack com backend Spring Boot (Java 17) e frontend Angular 14, implementando o teste técnico “SAT – Java/Angular”, com arquitetura limpa, validações, testes e UI com Angular Material.

## Requisitos
- Java 17
- Maven 3.8+
- Node 16 (recomendado) e npm

## Backend (Spring Boot)
Local: `backend/`

### Principais dependências
- spring-boot-starter-web, validation, data-jpa
- h2 (runtime), spring-boot-starter-test (JUnit 5, Mockito)
- lombok (opcional)

### Como executar
```
cd backend
mvn clean install
mvn spring-boot:run
```
App: `http://localhost:8080`  | H2 console: `http://localhost:8080/h2-console`

### Estrutura (pacotes)
- `com.sat.pedidos.api.controller` – controllers REST (`PedidoController`, `ProdutoController`, `ClienteController`)
- `com.sat.pedidos.api.dto` – DTOs (`PedidoDTO`, `PedidoItemDTO`)
- `com.sat.pedidos.api.handler` – `ApiExceptionHandler` (erros padronizados)
- `com.sat.pedidos.domain.model` – entidades (`Pedido`, `ItemPedido`, `Produto`, `Cliente`, `StatusPedido`)
- `com.sat.pedidos.domain.repository` – repositórios (inclui queries de existência para integridade)
- `com.sat.pedidos.domain.service`/`impl` – serviços (pedido, produto, cliente, notificação)
- `com.sat.pedidos.config` – CORS liberado para `http://localhost:4200`

### Regras de negócio e validações
- Pedido: itens obrigatórios; quantidade > 0; valor unitário > 0; `valor` total calculado (soma itens); `status` padrão LANCADO (editável); exclusão remove itens (cascade).
- Produto: nome obrigatório; preço > 0; bloqueia exclusão se usado em algum pedido.
- Cliente: nome obrigatório; documento (CPF/CNPJ) obrigatório e validado (algoritmo DV, normalizado para dígitos). Bloqueia exclusão se houver pedidos.

### Endpoints principais
- Pedidos: `POST /pedidos`, `GET /pedidos`, `GET /pedidos/{id}`, `PUT /pedidos/{id}`, `DELETE /pedidos/{id}`
- Produtos: `POST /produtos`, `GET /produtos`, `GET /produtos/{id}`, `PUT /produtos/{id}`, `DELETE /produtos/{id}`
- Clientes: `POST /clientes`, `GET /clientes`, `GET /clientes/{id}`, `PUT /clientes/{id}`, `DELETE /clientes/{id}`

#### Exemplo de criar Produto
```http
POST /produtos
{ "nome": "Celular X", "preco": 1999.90 }
```

#### Exemplo de criar Cliente
```http
POST /clientes
{ "nome": "Fulano", "documento": "61834931000180" }
```

#### Exemplo de criar Pedido (com itens)
```http
POST /pedidos
{
  "clienteId": 1,
  "status": "LANCADO",
  "itens": [
    { "produtoId": 1, "quantidade": 1, "valorUnitario": 1999.90, "numeroSerie": "IMEI-123" }
  ]
}
```

#### Exemplo de erro 400 (payload padronizado)
```json
{ "timestamp": "2025-01-01T00:00:00Z", "status": 400, "error": "Bad Request", "message": "Documento inválido (CPF/CNPJ)", "path": "/clientes" }
```

### Testes
```
cd backend
mvn test
```
Cobertura para serviços e controllers (JUnit 5, Mockito, MockMvc).

## Frontend (Angular 14)
Local: `frontend/`

### Stack
- Angular Material, HttpClient, ReactiveForms

### Como executar
```
cd frontend
npm install
npm start
```
App: `http://localhost:4200` | Backend: `http://localhost:8080` (config em `src/environments/environment.ts`)

### Funcionalidades implementadas
- Layout com menu lateral (toggle) e título dinâmico por rota.
- Pedidos: listagem com colunas `ID`, `Cliente` (nome), `Valor`, `Status`, `Data de criação`; filtros por cliente/documento/valor/status/data; ações por ícones (editar/excluir com diálogo de confirmação). Formulário com seleção de cliente e itens (produto, quantidade, valor unitário, número de série) e seletor de `Status`. Total calculado automaticamente.
- Produtos: listagem com filtro (nome/preço), ações por ícones (editar/excluir com confirmação). Formulário de cadastro/edição.
- Clientes: listagem com filtro (nome), ações por ícones (editar/excluir com confirmação). Formulário com `nome` e `CPF/CNPJ` (máscara no input). Erros de negócio exibidos via snackbar.

### Observações de UX
- Campos de seleção possuem campo de busca embutido.
- Painéis dos selects deslocados para baixo (evita sobreposição ao campo).

## Decisões de Arquitetura
- Camadas separadas; controller fino; service orquestra; repository persiste; `NotificacaoService` por interface (injeção de dependência).
- Validações: Bean Validation no DTO + validações de domínio no service.
- Tratamento de erros padronizado em `ApiExceptionHandler`.
- H2 em memória para facilitar execução local.
- CORS liberado para o frontend local.

## Troubleshooting
- Se o Angular falhar por tipos de Node muito novos, use Node 16 (recomendado) ou habilite `skipLibCheck` (já aplicado).
- Se não conseguir chamar a API, verifique se o backend está na porta 8080 e o CORS ativo.

## Scripts úteis
Backend:
```
mvn clean verify
mvn spring-boot:run
```
Frontend:
```
npm start
```

