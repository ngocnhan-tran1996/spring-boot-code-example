package com.springboot.code.example.transaction.multiple.datasource.config.jta;

import java.sql.SQLException;
import javax.sql.XADataSource;
import org.postgresql.xa.PGXADataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
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
      @Qualifier("wildDataSourceProperties") DataSourceProperties wildDataSourceProperties,
      @Qualifier("wildDatasource") HikariDataSource wildDatasource,
      @Qualifier("wildJpaProperties") JpaProperties wildJpaProperties) throws SQLException {

    var hibernateJpaVendorAdapter = new HibernateJpaVendorAdapter();
    hibernateJpaVendorAdapter.setShowSql(true);
    hibernateJpaVendorAdapter.setGenerateDdl(true);
    hibernateJpaVendorAdapter.setDatabase(Database.ORACLE);

    var datasource = this.chooseDataSource(wildDataSourceProperties);

    var xaDataSource = new AtomikosDataSourceBean();
    xaDataSource.setXaDataSource(datasource);
    xaDataSource.setUniqueResourceName("wild");
    xaDataSource.setMinPoolSize(wildDatasource.getMinimumIdle());
    xaDataSource.setMaxPoolSize(wildDatasource.getMaximumPoolSize());

    var entityManager = new LocalContainerEntityManagerFactoryBean();
    entityManager.setJtaDataSource(wildDatasource);
    entityManager.setJpaVendorAdapter(hibernateJpaVendorAdapter);
    entityManager.setPackagesToScan(BASE_PACKAGE);
    entityManager.setPersistenceUnitName("wild_jta");
    entityManager.setJpaPropertyMap(wildJpaProperties.getProperties());
    return entityManager;
  }

  private XADataSource chooseDataSource(
      DataSourceProperties dataSourceProperties)
      throws SQLException {

    if (dataSourceProperties.getDriverClassName().contains("postgres")) {

      var datasource = new PGXADataSource();
      datasource.setURL(dataSourceProperties.getUrl());
      datasource.setUser(dataSourceProperties.getUsername());
      datasource.setPassword(dataSourceProperties.getPassword());
      return datasource;
    }

    var datasource = new OracleXADataSource();
    datasource.setURL(dataSourceProperties.getUrl());
    datasource.setUser(dataSourceProperties.getUsername());
    datasource.setPassword(dataSourceProperties.getPassword());
    return datasource;
  }

}