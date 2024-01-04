package io.ngocnhan_tran1996.code.example.transaction.poly.datasource.config;

import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import com.atomikos.jdbc.AtomikosDataSourceBean;

@TestConfiguration
@EnableJpaRepositories(
    basePackages = JtaPostgresDataSourceConfig.BASE_PACKAGE,
    entityManagerFactoryRef = JtaPostgresDataSourceConfig.PERSISTENCE_UNIT,
    transactionManagerRef = "jtaTransactionManager")
public class JtaPostgresDataSourceConfig extends AbstractDataSourceConfig {

  public static final String BASE_PACKAGE =
      "io.ngocnhan_tran1996.code.example.transaction.poly.jta.datasource.postgres";
  public static final String PERSISTENCE_UNIT = "jtaPostgresEntityManager";

  @Primary
  @Bean("jtaPostgresDataSource")
  @ConfigurationProperties("app.datasource.postgres")
  AtomikosDataSourceBean datasource() {

    return new AtomikosDataSourceBean();
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
      @Qualifier("jtaPostgresDataSource") DataSource datasource,
      @Qualifier("postgresJpaProperties") JpaProperties jpaProperties) {

    return super.entityManager(datasource, jpaProperties);
  }

  @Bean("jtaPostgresJdbcTemplate")
  JdbcTemplate jdbcTemplate(@Qualifier("jtaPostgresDataSource") DataSource datasource) {

    return new JdbcTemplate(datasource);
  }

  @Override
  protected String[] scanPackages() {

    return new String[] {BASE_PACKAGE};
  }

  @Override
  protected String persistenceUnit() {

    return PERSISTENCE_UNIT;
  }

}