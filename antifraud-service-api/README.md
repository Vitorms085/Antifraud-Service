# Anti-Fraud Service API Module

Este módulo é responsável pela camada de API REST do serviço de anti-fraude.

## Responsabilidades

- Exposição dos endpoints REST
- Validação de entrada de dados
- Roteamento de requisições
- Tratamento de erros HTTP
- Configuração do Spring Boot

## Endpoints Disponíveis

### Health Check
```
GET /health
```
Verifica o status da aplicação.

### Transações Bancárias
```
POST /transactions
```
Endpoint para processar novas transações bancárias.

Exemplo de payload:
```json
{
  "accountId": "123",
  "value": 1000.00,
  "type": "CREDIT"
}
```

### Contas Bancárias
```
GET /accounts/{id}
```
Consulta informações de uma conta bancária.

## Configuração

As principais configurações estão no arquivo `application.properties`:

- Configurações do banco de dados
- Configurações do Kafka
- Configurações do Flyway
- Outras configurações do Spring

## Como Executar

Do diretório raiz do projeto:
```bash
../gradlew bootRun
```

## Testes

Para executar os testes deste módulo:
```bash
../gradlew :antifraud-service-api:test
```

### Testes Disponíveis

- `AnalysisControllerTest`
- `BankAccountControllerTest`
- `BankTransactionControllerTest`

## Dependências

- Spring Boot Web
- Spring Boot Data JPA
- Spring Kafka
- PostgreSQL Driver
- Flyway Migration
