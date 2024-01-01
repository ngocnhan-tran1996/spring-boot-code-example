package com.springboot.code.example.transaction.poly.datasource.config;

import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import com.zaxxer.hikari.HikariDataSource;

@TestConfiguration
@EnableJpaRepositories(
    basePackages = PostgresDataSourceConfig.BASE_PACKAGE,
    entityManagerFactoryRef = PostgresDataSourceConfig.PERSISTENCE_UNIT,
    transactionManagerRef = PostgresDataSourceConfig.TRANSACTION_MANAGER)
public class PostgresDataSourceConfig extends AbstractDataSourceConfig {

  public static final String BASE_PACKAGE =
      "com.springboot.code.example.transaction.domain";
  public static final String PERSISTENCE_UNIT = "postgresEntityManager";
  public static final String TRANSACTION_MANAGER = "postgresTransactionManager";

  @Override
  protected String[] scanPackages() {

    return new String[] {BASE_PACKAGE};
  }

  @Override
  protected String persistenceUnit() {

    return PERSISTENCE_UNIT;
  }

  @Primary
  @Bean
  @ConfigurationProperties("app.datasource.postgres")
  @Override
  public DataSourceProperties dataSourceProperties() {

    return super.dataSourceProperties();
  }

  @Primary
  @Bean
  @ConfigurationProperties("app.datasource.postgres.hikari")
  @Override
  public HikariDataSource datasource(DataSourceProperties dataSourceProperties) {

    return super.datasource(dataSourceProperties);
  }

  @Primary
  @Bean("postgresJpaProperties")
  @ConfigurationProperties("app.datasource.postgres.jpa")
  @Override
  public JpaProperties jpaProperties() {

    return super.jpaProperties();
  }

  @Primary
  @Bean(PERSISTENCE_UNIT)
  @Override
  public LocalContainerEntityManagerFactoryBean entityManager(
      HikariDataSource datasource,
      JpaProperties jpaProperties) {

    return super.entityManager(datasource, jpaProperties);
  }

  @Primary
  @Bean(TRANSACTION_MANAGER)
  @Override
  public PlatformTransactionManager transactionManager(
      LocalContainerEntityManagerFactoryBean entityManager) {

    return super.transactionManager(entityManager);
  }

}