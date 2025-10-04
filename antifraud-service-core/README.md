# Anti-Fraud Service Core Module

Este módulo contém a lógica de negócio central do serviço de anti-fraude.

## Responsabilidades

- Implementação das regras de negócio
- Processamento de transações
- Detecção de fraudes
- Validação de limites
- Integração com Kafka para eventos
- Gerenciamento de contas bancárias

## Principais Serviços

- **BankTransactionService**: Processamento de transações bancárias
- **BankTransactionValidatorService**: Validação de regras de negócio para transações
- **SuspiciousTransactionService**: Gerenciamento de transações suspeitas
- **BankAccountService**: Gerenciamento de contas bancárias

## Regras de Negócio

O módulo implementa as seguintes verificações de fraude:

1. Limite de crédito excedido
2. Limite de débito excedido
3. Transações suspeitas em intervalo curto de tempo
4. Análise de padrões de comportamento

## Eventos Kafka

### Produção
- Eventos de transações suspeitas
- Notificações de fraude

### Consumo
- Eventos de transações bancárias

## Como Testar

Para executar os testes deste módulo:
```bash
  ../gradlew :antifraud-service-core:test
```

### Testes Disponíveis

- `BankAccountServiceTest`
- `BankTransactionServiceTest`
- `BankTransactionValidatorServiceTest`
- `SuspiciousTransactionServiceTest`

## Dependências

- Spring Boot
- Spring Data JPA
- Spring Kafka
- Model Module (dependência interna)
