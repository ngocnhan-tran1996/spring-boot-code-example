# Use EntityManager with Spring 2.7.9

## I. Requirement
- Java 17 or higher
- Maven 3.8.5 or higher

## I. Overview

This is a simple demo about how to use `EntityManager` for inserting and selecting database.

Database in use: H2

## II. Installation and Getting Started

Choose one of below comment lines and config 2 files for a complete application in Java:

### 1. `application.yaml`

  ```yaml
    spring:
    profiles:
        active: multiple # for multiple data source
        active: single # for single data source

    h2:
        console:
        enabled: true
  ```

### 2. `EntityManagerApplication.java`

  ```java

    @SpringBootApplication
    public class EntityManagerApplication {

        public static void main(String[] args) {
            SpringApplication.run(EntityManagerApplication.class, args);
        }

        // for multiple data source with annotation @Transactional
        @Resource(name = "multipleEntityManagerWithAnnotationService")
        // for multiple data source without annotation @Transactional
        @Resource(name = "multipleEntityManagerWithoutAnnotationService")
        // for single data source with annotation @Transactional
        @Resource(name = "singleEntityManagerService")
        EntityManagerService entityManagerService;

        @Bean
        CommandLineRunner commandLineRunner() {
            return args -> entityManagerService.findAll();
        }

    }
  ```