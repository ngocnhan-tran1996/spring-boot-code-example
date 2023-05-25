# Spring Boot Code Example

## I. Requirement
- Java 17 or higher
- Maven 3.8.5 or higher

## II. Spring version *3.0.x*

Modules:

```
    └── springboot-common
    └── springboot-database
    └── springboot-docker
    └── springboot-elastic
    └── springboot-log4j2
```

1. `springboot-common`: helper or utility package
2. `springboot-database`: 

    - config multiple datasource 
    - call procedure and function via jdbc
    - pagination by `EntityManager`

3. `springboot-docker` contains `docker-compose.yml` for this source
4. `springboot-elastic`:

    - config elastic, kibana, apm and logstash
    - trace consumer with transaction name `RabbitMQ receive from queueName`
5. `springboot-log4j2`:

    - auto add uuid when receive message
    - config save file log json format
    - mask sensitive data by format key-value