# Use EntityManager with Spring 2.7.9

## I. Requirement
- Java 17 or higher
- Maven 3.8.5 or higher

## I. Overview

This is a simple demo about using native query.

Database in use: H2

## II. Installation and Getting Started

### 1. `Using entityManager for pagination`

Demo in file `CardEntityManagerService.java`

Reference in package `com.springboot.datasource.common.pagination`

**How to use**
  ```java
      // init bean
      @Bean
      EntityManagerPagination entityManagerPagination(EntityManager entityManager) {

        return DefaultEntityManagerPagination.create(entityManager);
      }

      // inject bean
      entityManagerPagination.query("SELECT id, name FROM {h-schema}CAR")
          .ofPageRequest(page, size)
          .getPages(CarEntity.class);
  ```