# Anti-Fraud Service

Este é um serviço de detecção de fraudes em transações bancárias, desenvolvido com Spring Boot, PostgreSQL e Kafka.

## Arquitetura do Projeto

O projeto está dividido em três módulos principais:

- **antifraud-service-api**: Módulo responsável pela exposição dos endpoints REST e interface com o usuário
- **antifraud-service-core**: Módulo que contém a lógica de negócio e serviços
- **antifraud-service-model**: Módulo com as entidades e modelos de dados

## Pré-requisitos

- Java 17+
- Docker e Docker Compose
- Gradle 8.x (ou use o wrapper incluído no projeto)

## Tecnologias Utilizadas

- Spring Boot
- PostgreSQL
- Apache Kafka
- Flyway para migrations
- JUnit e Mockito para testes

## Como Executar

1. Clone o repositório:
```bash
git clone [url-do-repositorio]
cd antifraud-service
```

2. Inicie os serviços de infraestrutura (PostgreSQL e Kafka):
```bash
docker-compose up -d
```

3. Execute a aplicação usando o Gradle wrapper:

No Windows (PowerShell ou CMD):
```bash
.\gradlew :antifraud-service-api:bootRun
```

No Linux/MacOS:
```bash
./gradlew :antifraud-service-api:bootRun
```

A aplicação estará disponível em `http://localhost:8080`

> **Nota**: Certifique-se de que o comando seja executado a partir do diretório raiz do projeto (antifraud-service).

## Infraestrutura

O projeto utiliza os seguintes serviços:

- **PostgreSQL**
  - Host: localhost
  - Porta: 5432
  - Database: antifraud
  - Usuário: admin
  - Senha: admin123

- **Kafka**
  - Bootstrap Servers: localhost:9092
  - Tópicos:
    - transacoes.events: Eventos de transações
    - fraud.suspictions.notifications: Notificações de suspeitas de fraude

## Estrutura de Módulos

Cada módulo possui seu próprio README com detalhes específicos:

- [API Module](./antifraud-service-api/README.md)
- [Core Module](./antifraud-service-core/README.md)
- [Model Module](./antifraud-service-model/README.md)

## Execução dos Testes

Para executar os testes de todos os módulos:

```bash
  ./gradlew test
```

## Health Check

A aplicação possui um endpoint de health check:
```
GET /health
```

## Documentação Adicional

Consulte os READMEs específicos de cada módulo para mais detalhes sobre suas funcionalidades e responsabilidades.
