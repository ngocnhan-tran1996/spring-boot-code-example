package com.springboot.code.example.database.multiple.datasource.config;

import java.sql.SQLException;
import org.hibernate.engine.transaction.jta.platform.internal.AtomikosJtaPlatform;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jta.atomikos.AtomikosDataSourceBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import com.zaxxer.hikari.HikariDataSource;
import oracle.jdbc.xa.client.OracleXADataSource;

@Configuration
@EnableJpaRepositories(
    basePackages = WildDatasourceConfig.BASE_PACKAGE,
    entityManagerFactoryRef = "wildEntityManager",
    transactionManagerRef = "transactionManager")
public class WildDatasourceConfig {

  public static final String BASE_PACKAGE =
      "com.springboot.code.example.database.multiple.datasource.wild";

  @Bean
  @ConfigurationProperties("app.datasource.wild")
  DataSourceProperties wildDataSourceProperties() {

    return new DataSourceProperties();
  }

  @Bean
  @ConfigurationProperties("app.datasource.wild.hikari")
  HikariDataSource wildDatasource() {

    return wildDataSourceProperties()
        .initializeDataSourceBuilder()
        .type(HikariDataSource.class)
        .build();
  }

  @Bean
  @ConfigurationProperties("app.datasource.wild.jpa")
  JpaProperties wildJpaProperties() {
    return new JpaProperties();
  }

  @Bean
  LocalContainerEntityManagerFactoryBean wildEntityManager(JpaVendorAdapter jpaVendorAdapter)
      throws SQLException {

    var datasource = new OracleXADataSource();
    datasource.setURL(wildDataSourceProperties().getUrl());
    datasource.setUser(wildDataSourceProperties().getUsername());
    datasource.setPassword(wildDataSourceProperties().getPassword());

    var xaDataSource = new AtomikosDataSourceBean();
    xaDataSource.setXaDataSourceClassName(wildDataSourceProperties().getDriverClassName());
    xaDataSource.setXaDataSource(datasource);
    xaDataSource.setUniqueResourceName("wild");
    xaDataSource.setMinPoolSize(wildDatasource().getMinimumIdle());
    xaDataSource.setMaxPoolSize(wildDatasource().getMaximumPoolSize());

    wildJpaProperties().getProperties().put("hibernate.transaction.jta.platform",
        AtomikosJtaPlatform.class.getName());
    wildJpaProperties().getProperties().put("javax.persistence.transactionType", "JTA");

    var entityManager = new LocalContainerEntityManagerFactoryBean();
    entityManager.setDataSource(wildDatasource());
    entityManager.setJtaDataSource(xaDataSource);
    entityManager.setJpaVendorAdapter(jpaVendorAdapter);
    entityManager.setPackagesToScan(BASE_PACKAGE);
    entityManager.setPersistenceUnitName("wild");
    entityManager.setJpaPropertyMap(wildJpaProperties().getProperties());
    return entityManager;
  }

}