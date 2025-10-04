# Anti-Fraud Service Model Module

Este módulo contém as entidades e modelos de dados utilizados em todo o sistema anti-fraude.

## Responsabilidades

- Definição das entidades JPA
- Modelos de dados (DTOs)
- Mapeamentos entre entidades e DTOs
- Definição de enums e constantes

## Principais Entidades

### BaseEntity
Classe base abstrata que fornece atributos comuns para todas as entidades:
- ID
- Data de criação
- Data de atualização

### BankAccount
Entidade que representa uma conta bancária no sistema:
- Informações do titular
- Limites de crédito/débito
- Status da conta

### BankTransaction
Entidade que representa uma transação bancária:
- Conta de origem
- Valor
- Tipo (CREDIT/DEBIT)
- Status
- Data/hora

### SuspiciousBankTransaction
Entidade que representa uma transação suspeita:
- Transação original
- Motivo da suspeita
- Nível de risco
- Status da análise

## Uso

Este módulo é uma dependência dos módulos API e Core, fornecendo as estruturas de dados básicas para todo o sistema.

## Como Testar

Para executar os testes deste módulo:
```bash
../gradlew :antifraud-service-model:test
```

## Dependências

- JPA/Hibernate
- PostgreSQL
- Lombok
- Validation API

## Boas Práticas

- Todas as entidades estendem BaseEntity
- Uso de validações através de annotations
- Implementação do padrão Builder via Lombok
- Auditoria automática de datas de criação/atualização
