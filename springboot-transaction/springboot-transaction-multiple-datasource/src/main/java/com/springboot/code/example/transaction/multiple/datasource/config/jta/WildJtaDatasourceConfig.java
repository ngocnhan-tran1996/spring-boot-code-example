package com.springboot.code.example.transaction.multiple.datasource.config.jta;

import java.sql.SQLException;
import java.util.Properties;
import org.postgresql.xa.PGXADataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import com.atomikos.spring.AtomikosDataSourceBean;
import com.zaxxer.hikari.HikariDataSource;
import oracle.jdbc.xa.client.OracleXADataSource;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
    basePackages = WildJtaDatasourceConfig.BASE_PACKAGE,
    entityManagerFactoryRef = "wildJtaEntityManager",
    transactionManagerRef = "transactionManager")
public class WildJtaDatasourceConfig {

  public static final String BASE_PACKAGE =
      "com.springboot.code.example.transaction.multiple.datasource.jta.wild";

  @Bean
  LocalContainerEntityManagerFactoryBean wildJtaEntityManager(
      @Qualifier("wildDatasource") HikariDataSource wildDatasource,
      @Qualifier("wildJpaProperties") JpaProperties wildJpaProperties) throws SQLException {

    var properties = new Properties();
    properties.setProperty("URL", wildDatasource.getJdbcUrl());
    properties.setProperty("user", wildDatasource.getUsername());
    properties.setProperty("password", wildDatasource.getPassword());

    String xaDataSourceClassName = wildDatasource.getJdbcUrl().contains("oracle")
        ? OracleXADataSource.class.getName()
        : PGXADataSource.class.getName();

    var xaDataSource = new AtomikosDataSourceBean();
    xaDataSource.setXaDataSourceClassName(xaDataSourceClassName);
    xaDataSource.setUniqueResourceName("wild");
    xaDataSource.setMinPoolSize(wildDatasource.getMinimumIdle());
    xaDataSource.setMaxPoolSize(wildDatasource.getMaximumPoolSize());
    xaDataSource.setXaProperties(properties);

    var entityManager = new LocalContainerEntityManagerFactoryBean();
    entityManager.setJtaDataSource(xaDataSource);
    entityManager.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
    entityManager.setPackagesToScan(BASE_PACKAGE);
    entityManager.setPersistenceUnitName(xaDataSource.getUniqueResourceName());
    entityManager.setJpaPropertyMap(wildJpaProperties.getProperties());
    return entityManager;
  }

}