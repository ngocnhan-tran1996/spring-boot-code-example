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
import com.atomikos.spring.AtomikosDataSourceBean;
import com.zaxxer.hikari.HikariDataSource;
import oracle.jdbc.xa.client.OracleXADataSource;

@Configuration
@EnableJpaRepositories(
    basePackages = VehicleJtaDatasourceConfig.BASE_PACKAGE,
    entityManagerFactoryRef = "vehicleJtaEntityManager",
    transactionManagerRef = "transactionManager")
public class VehicleJtaDatasourceConfig {

  public static final String BASE_PACKAGE =
      "com.springboot.code.example.transaction.multiple.datasource.jta.vehicle";

  @Bean
  LocalContainerEntityManagerFactoryBean vehicleJtaEntityManager(
      @Qualifier("vehicleDataSourceProperties") DataSourceProperties vehicleDataSourceProperties,
      @Qualifier("vehicleDatasource") HikariDataSource vehicleDatasource,
      @Qualifier("vehicleJpaProperties") JpaProperties vehicleJpaProperties,
      JpaVendorAdapter jpaVendorAdapter)
      throws SQLException {

    var datasource = new OracleXADataSource();
    datasource.setURL(vehicleDataSourceProperties.getUrl());
    datasource.setUser(vehicleDataSourceProperties.getUsername());
    datasource.setPassword(vehicleDataSourceProperties.getPassword());

    var xaDataSource = new AtomikosDataSourceBean();
    xaDataSource.setXaDataSourceClassName(vehicleDataSourceProperties.getDriverClassName());
    xaDataSource.setXaDataSource(datasource);
    xaDataSource.setUniqueResourceName("vehicle");
    xaDataSource.setMinPoolSize(vehicleDatasource.getMinimumIdle());
    xaDataSource.setMaxPoolSize(vehicleDatasource.getMaximumPoolSize());

    var entityManager = new LocalContainerEntityManagerFactoryBean();
    entityManager.setDataSource(vehicleDatasource);
    entityManager.setJtaDataSource(xaDataSource);
    entityManager.setJpaVendorAdapter(jpaVendorAdapter);
    entityManager.setPackagesToScan(BASE_PACKAGE);
    entityManager.setPersistenceUnitName("vehicle");
    entityManager.setJpaPropertyMap(vehicleJpaProperties.getProperties());
    return entityManager;
  }

}