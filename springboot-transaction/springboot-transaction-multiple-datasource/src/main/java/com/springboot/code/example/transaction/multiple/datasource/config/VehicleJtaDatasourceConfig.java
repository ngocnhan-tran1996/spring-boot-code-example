package com.springboot.code.example.transaction.multiple.datasource.config;

import java.util.Properties;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.jta.atomikos.AtomikosDataSourceBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
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

  @Primary
  @Bean
  LocalContainerEntityManagerFactoryBean vehicleJtaEntityManager(
      @Qualifier("vehicleDatasource") HikariDataSource vehicleDatasource,
      @Qualifier("vehicleJpaProperties") JpaProperties vehicleJpaProperties) {

    var properties = new Properties();
    properties.setProperty("URL", vehicleDatasource.getJdbcUrl());
    properties.setProperty("user", vehicleDatasource.getUsername());
    properties.setProperty("password", vehicleDatasource.getPassword());

    var xaDataSource = new AtomikosDataSourceBean();
    xaDataSource.setXaDataSourceClassName(OracleXADataSource.class.getName());
    xaDataSource.setUniqueResourceName("vehicle");
    xaDataSource.setMinPoolSize(vehicleDatasource.getMinimumIdle());
    xaDataSource.setMaxPoolSize(vehicleDatasource.getMaximumPoolSize());
    xaDataSource.setXaProperties(properties);

    var entityManager = new LocalContainerEntityManagerFactoryBean();
    entityManager.setJtaDataSource(xaDataSource);
    entityManager.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
    entityManager.setPackagesToScan(BASE_PACKAGE);
    entityManager.setPersistenceUnitName(xaDataSource.getUniqueResourceName());
    entityManager.setJpaPropertyMap(vehicleJpaProperties.getProperties());
    return entityManager;
  }

}