package io.ngocnhan_tran1996.code.example.database.poly.datasource.config;

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
    basePackages = PostgresDataSourceConfig.BASE_PACKAGE,
    entityManagerFactoryRef = PostgresDataSourceConfig.PERSISTENCE_UNIT,
    transactionManagerRef = PostgresDataSourceConfig.TRANSACTION_MANAGER)
public class PostgresDataSourceConfig extends AbstractDataSourceConfig {

  public static final String BASE_PACKAGE =
      "io.ngocnhan_tran1996.code.example.database.poly.datasource.postgres";
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

  @Bean("postgresDataSourceProperties")
  @ConfigurationProperties("app.datasource.db-postgres")
  @Override
  public DataSourceProperties dataSourceProperties() {

    return super.dataSourceProperties();
  }

  @Bean("postgresDataSource")
  @ConfigurationProperties("app.datasource.db-postgres.hikari")
  @Override
  public HikariDataSource datasource(
      @Qualifier("postgresDataSourceProperties") DataSourceProperties dataSourceProperties) {

    return super.datasource(dataSourceProperties);
  }

  @Bean("postgresJpaProperties")
  @ConfigurationProperties("app.datasource.db-postgres.jpa")
  @Override
  public JpaProperties jpaProperties() {

    return super.jpaProperties();
  }

  @Bean(PERSISTENCE_UNIT)
  @Override
  public LocalContainerEntityManagerFactoryBean entityManager(
      @Qualifier("postgresDataSource") HikariDataSource datasource,
      @Qualifier("postgresJpaProperties") JpaProperties jpaProperties) {

    return super.entityManager(datasource, jpaProperties);
  }

  @Bean(TRANSACTION_MANAGER)
  @Override
  public PlatformTransactionManager transactionManager(
      @Qualifier(PERSISTENCE_UNIT) LocalContainerEntityManagerFactoryBean entityManager) {

    return super.transactionManager(entityManager);
  }

}