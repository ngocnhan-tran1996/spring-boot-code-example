package io.ngocnhan_tran1996.code.example.transaction.poly.datasource.config;

import javax.sql.DataSource;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

interface DataSourceConfig {

  DataSourceProperties dataSourceProperties();

  DataSource datasource(DataSourceProperties dataSourceProperties);

  JpaProperties jpaProperties();

  LocalContainerEntityManagerFactoryBean entityManager(
      DataSource datasource,
      JpaProperties jpaProperties);

  PlatformTransactionManager transactionManager(
      LocalContainerEntityManagerFactoryBean entityManager);

}
