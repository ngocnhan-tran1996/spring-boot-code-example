# Welcome to My Spring Boot Code Example

## About me

Hi, I’m Nhan and I’m a Java developer, so I’m very (x10) lazy. This is why I create a spring boot example, this Spring Boot Code Example for future…​ or maybe I just copy when I feel tired

## What You Need

* [Java 17](https://www.oracle.com/java/technologies/downloads/) or higher
* [Maven 3.8.5](https://maven.apache.org/download.cgi/) or higher
* [Docker](https://www.docker.com/products/docker-desktop/)
* [Spring Boot version **3.0.x**](https://spring.io/)

## Overview

### Structure of source

```
`spring-boot-code-example`
├── docker
├── docs
├── springboot-common
├── springboot-database
│   ├── springboot-database-entity-manager
│   ├── springboot-database-jdbc
│   └── springboot-database-multiple-datasource
├── .dockerignore
├── .gitignore
├── README.md
└── pom.xml
```

### Modules's Feature

1. `docker`: contains `docker-compose.yml` which use in whole repository
2. `springboot-common`: helper or utility package
3. `springboot-database`: some code example relative database