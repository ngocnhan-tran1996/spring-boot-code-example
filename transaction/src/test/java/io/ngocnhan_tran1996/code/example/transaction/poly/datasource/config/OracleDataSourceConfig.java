package io.ngocnhan_tran1996.code.example.transaction.poly.datasource.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import com.zaxxer.hikari.HikariDataSource;

@TestConfiguration
@EnableJpaRepositories(
    basePackages = OracleDataSourceConfig.BASE_PACKAGE,
    entityManagerFactoryRef = OracleDataSourceConfig.PERSISTENCE_UNIT,
    transactionManagerRef = OracleDataSourceConfig.TRANSACTION_MANAGER)
public class OracleDataSourceConfig extends AbstractDataSourceConfig {

  public static final String BASE_PACKAGE =
      "io.ngocnhan_tran1996.code.example.transaction.poly.datasource.oracle";
  public static final String PERSISTENCE_UNIT = "oracleEntityManager";
  public static final String TRANSACTION_MANAGER = "oracleTransactionManager";

  @Override
  protected String[] scanPackages() {

    return new String[] {BASE_PACKAGE};
  }

  @Override
  protected String persistenceUnit() {

    return PERSISTENCE_UNIT;
  }

  @Bean("oracleDataSourceProperties")
  @ConfigurationProperties("app.datasource.oracle")
  @Override
  public DataSourceProperties dataSourceProperties() {

    return super.dataSourceProperties();
  }

  @Bean("oracleDataSource")
  @ConfigurationProperties("app.datasource.oracle.hikari")
  @Override
  public HikariDataSource datasource(
      @Qualifier("oracleDataSourceProperties") DataSourceProperties dataSourceProperties) {

    return super.datasource(dataSourceProperties);
  }

  @Bean("oracleJpaProperties")
  @ConfigurationProperties("app.datasource.oracle.jpa")
  @Override
  public JpaProperties jpaProperties() {

    return super.jpaProperties();
  }

  @Bean(PERSISTENCE_UNIT)
  @Override
  public LocalContainerEntityManagerFactoryBean entityManager(
      @Qualifier("oracleDataSource") HikariDataSource datasource,
      @Qualifier("oracleJpaProperties") JpaProperties jpaProperties) {

    return super.entityManager(datasource, jpaProperties);
  }

  @Bean(TRANSACTION_MANAGER)
  @Override
  public PlatformTransactionManager transactionManager(
      @Qualifier(PERSISTENCE_UNIT) LocalContainerEntityManagerFactoryBean entityManager) {

    return super.transactionManager(entityManager);
  }

}