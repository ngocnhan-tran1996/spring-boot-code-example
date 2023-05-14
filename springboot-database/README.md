# Some example around database with Spring 2.7.x

## I. Requirement
- Java 17 or higher
- Maven 3.8.5 or higher

## I. Overview

This is a simple demo about manipulate database.

Database in use: **H2, ORACLE**

## II. Installation

### 0. **Installation**

    - [Docker](https://www.docker.com/)

### 1. **Oracle Database**

    **Step 1:** Run `springboot-docker/docker-compose.yml` to start oracle database. Remove unused container if you don't need it

    ```powershell
        spring-boot-code-example> docker compose -f "springboot-docker\docker-compose.yml" up -d --build
    ```

    **Step 2:** Run script in order with user `sys` in folder `springboot-database\src\main\resources\jdbc`


## III. Getting Started

### 1. Multiple DataSource

- Database in use: **H2**

- Code Structure

    ```
        com
        +- springboot
            +- code
                +- example
                    +- database
                        +- DatabaseApplication.java
                        |
                        +- multiple
                            +- datasource
                                +- DatasourceExecutor.java
                                |
                                +- config
                                |   +- VehicleDatasourceConfig.java
                                |   +- WildDatasourceConfig.java
                                |
                                +- entity
                                |   +- BaseEntity.java
                                |
                                +- vehicle
                                |   +- CardRepository.java
                                |   +- CarEntity.java
                                |
                                +- wild
                                    +- AnimalEntity.java
                                    +- AnimalRepository.java
    ```

- Config `application.yaml`

    ```yaml
        spring:
            application:
                name: springboot-database
            profiles:
                active: multiple-datasource
    ```

- Config yaml file: `application-multiple-datasource.yaml`

- Run test service layer: `DatasourceExecutorTest.java`

- Run test database layer:

    - `AnimalRepositoryTest.java`
    - `CardRepositoryTest.java`

- Copy below code and paste into `DatabaseApplication.java` to run application:

    ```java
        package com.springboot.code.example.database;

        import org.springframework.boot.SpringApplication;
        import org.springframework.boot.autoconfigure.SpringBootApplication;
        import com.springboot.code.example.database.profiles.ProfilePackages;

        @SpringBootApplication(scanBasePackages = ProfilePackages.MULTIPLE_DATASOURCE)
        public class DatabaseApplication {

            public static void main(String[] args) {
                SpringApplication.run(DatabaseApplication.class, args);
            }

        }
    ```

### 2. Entity Manager for pagination and CRUD (only insert and select) to database

- Database in use: **Oracle**

- Code Structure

    ```
        com
        +- springboot
            +- code
                +- example
                    +- database
                        +- DatabaseApplication.java
                        |
                        +- entitymanager
                            +- EntityManagerExecutor.java
    ```

- Config `application.yaml`

    ```yaml
        spring:
            application:
                name: springboot-database
            profiles:
                active: entitymanager
    ```

- Config yaml file: `application-entitymanager.yaml`

- Run test service layer: `EntityManagerExecutorTest.java`

- Run test database layer: `EntityManagerExecutorDatabaseLayerTest.java`

- Copy below code and paste into `DatabaseApplication.java` to run application:

    ```java
        package com.springboot.code.example.database;

        import org.springframework.boot.SpringApplication;
        import org.springframework.boot.autoconfigure.SpringBootApplication;
        import com.springboot.code.example.database.profiles.ProfilePackages;

        @SpringBootApplication(scanBasePackages = ProfilePackages.ENTITY_MANAGER)
        public class DatabaseApplication {

            public static void main(String[] args) {
                SpringApplication.run(DatabaseApplication.class, args);
            }

        }
    ```

### 3. JDBC for calling Procedure and Function

- Database in use: **Oracle**

- Code Structure

    ```
        com
        +- springboot
            +- code
                +- example
                    +- database
                        +- DatabaseApplication.java
                        |
                        +- jdbc
                            +- JdbcTemplateExecutor.java
                            +- OracleJdbcTemplateExecutor.java
    ```

- Config `application.yaml`

    ```yaml
        spring:
            application:
                name: springboot-database
            profiles:
                active: jdbc
    ```

- Config yaml file: `application-jdbc.yaml`

- Run test service layer:

    - `JdbcTemplateExecutorTest.java`
    - `OracleJdbcTemplateExecutorTest.java`

- Run test database layer:

    - `JdbcTemplateExecutorDatabaseLayerTest.java`
    - `OracleJdbcTemplateExecutorDatabaseLayerTest.java`

- Copy below code and paste into `DatabaseApplication.java` to run application:

    ```java
        package com.springboot.code.example.database;

        import org.springframework.boot.SpringApplication;
        import org.springframework.boot.autoconfigure.SpringBootApplication;
        import com.springboot.code.example.database.profiles.ProfilePackages;

        @SpringBootApplication(scanBasePackages = ProfilePackages.JDBC)
        public class DatabaseApplication {

            public static void main(String[] args) {
                SpringApplication.run(DatabaseApplication.class, args);
            }

        }
    ```

## IV Reference Guide

1. [Multiple DataSource](https://www.javaguides.net/2018/09/spring-boot-jpa-multiple-data-sources-example.html)
2. [For loop in Oracle](https://blogs.oracle.com/connect/post/on-looping-first-and-last)
3. [Get metadata without deprecated StructDescriptor in Oracle](https://stackoverflow.com/questions/50697679/get-metadata-without-deprecated-structdescriptor-in-oracle)
4. [Source code handle complex type oracle](https://github.com/spring-attic/spring-data-jdbc-ext/blob/master/spring-data-oracle/src/main/java/org/springframework/data/jdbc/support/oracle/SqlReturnArray.java)
5. [Test SimpleJdbcCall](https://github.com/spring-projects/spring-framework/blob/6.0.x/spring-jdbc/src/test/java/org/springframework/jdbc/core/simple/SimpleJdbcCallTests.java)
6. [Constant for testing SimpleJdbcCall](https://docs.oracle.com/en/java/javase/17/docs/api//constant-values.html#java.sql.DatabaseMetaData.procedureColumnOut)
7. [Returning REF Cursor from a SimpleJdbcCall](https://www.logicbig.com/tutorials/spring-framework/spring-data-access-with-jdbc/simple-jdbc-ref-cursor.html)