package com.springboot.code.example.transaction.poly.datasource.config;

import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import com.zaxxer.hikari.HikariDataSource;

interface DataSourceConfig {

  DataSourceProperties dataSourceProperties();

  HikariDataSource datasource(DataSourceProperties dataSourceProperties);

  JpaProperties jpaProperties();

  LocalContainerEntityManagerFactoryBean entityManager(
      HikariDataSource datasource,
      JpaProperties jpaProperties);

  PlatformTransactionManager transactionManager(
      LocalContainerEntityManagerFactoryBean entityManager);

}
