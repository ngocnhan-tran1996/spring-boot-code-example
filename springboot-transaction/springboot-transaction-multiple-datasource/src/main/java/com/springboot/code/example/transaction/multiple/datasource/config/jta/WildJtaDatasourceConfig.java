package com.springboot.code.example.transaction.multiple.datasource.config.jta;

import java.sql.SQLException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
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
      @Qualifier("wildDataSourceProperties") DataSourceProperties wildDataSourceProperties,
      @Qualifier("wildDatasource") HikariDataSource wildDatasource,
      @Qualifier("wildJpaProperties") JpaProperties wildJpaProperties,
      JpaVendorAdapter jpaVendorAdapter)
      throws SQLException {

    var datasource = new OracleXADataSource();
    datasource.setURL(wildDataSourceProperties.getUrl());
    datasource.setUser(wildDataSourceProperties.getUsername());
    datasource.setPassword(wildDataSourceProperties.getPassword());

    var xaDataSource = new AtomikosDataSourceBean();
    xaDataSource.setXaDataSourceClassName(wildDataSourceProperties.getDriverClassName());
    xaDataSource.setXaDataSource(datasource);
    xaDataSource.setUniqueResourceName("wild");
    xaDataSource.setMinPoolSize(wildDatasource.getMinimumIdle());
    xaDataSource.setMaxPoolSize(wildDatasource.getMaximumPoolSize());

    var entityManager = new LocalContainerEntityManagerFactoryBean();
    entityManager.setDataSource(wildDatasource);
    entityManager.setJtaDataSource(xaDataSource);
    entityManager.setJpaVendorAdapter(jpaVendorAdapter);
    entityManager.setPackagesToScan(BASE_PACKAGE);
    entityManager.setPersistenceUnitName("wild");
    entityManager.setJpaPropertyMap(wildJpaProperties.getProperties());
    return entityManager;
  }

}