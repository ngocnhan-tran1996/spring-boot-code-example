package com.springboot.code.example.database.multiple.datasource.config;

import java.sql.SQLException;
import org.hibernate.engine.transaction.jta.platform.internal.AtomikosJtaPlatform;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import com.atomikos.jdbc.AtomikosDataSourceBean;
import com.zaxxer.hikari.HikariDataSource;
import oracle.jdbc.xa.client.OracleXADataSource;

@Configuration
@EnableJpaRepositories(
    basePackages = VehicleDatasourceConfig.BASE_PACKAGE,
    entityManagerFactoryRef = "vehicleEntityManager",
    transactionManagerRef = "transactionManager")
public class VehicleDatasourceConfig {

  public static final String BASE_PACKAGE =
      "com.springboot.code.example.database.multiple.datasource.vehicle";

  @Bean
  @ConfigurationProperties("app.datasource.vehicle")
  DataSourceProperties vehicleDataSourceProperties() {

    return new DataSourceProperties();
  }

  @Primary
  @Bean
  @ConfigurationProperties("app.datasource.vehicle.hikari")
  HikariDataSource vehicleDatasource() {

    return vehicleDataSourceProperties()
        .initializeDataSourceBuilder()
        .type(HikariDataSource.class)
        .build();
  }

  @Primary
  @Bean
  @ConfigurationProperties("app.datasource.vehicle.jpa")
  JpaProperties vehicleJpaProperties() {

    return new JpaProperties();
  }

  @Primary
  @Bean
  LocalContainerEntityManagerFactoryBean vehicleEntityManager(JpaVendorAdapter jpaVendorAdapter)
      throws SQLException {

    var datasource = new OracleXADataSource();
    datasource.setURL(vehicleDataSourceProperties().getUrl());
    datasource.setUser(vehicleDataSourceProperties().getUsername());
    datasource.setPassword(vehicleDataSourceProperties().getPassword());

    var xaDataSource = new AtomikosDataSourceBean();
    xaDataSource.setXaDataSourceClassName(vehicleDataSourceProperties().getDriverClassName());
    xaDataSource.setXaDataSource(datasource);
    xaDataSource.setUniqueResourceName("vehicle");
    xaDataSource.setMinPoolSize(vehicleDatasource().getMinimumIdle());
    xaDataSource.setMaxPoolSize(vehicleDatasource().getMaximumPoolSize());

    vehicleJpaProperties().getProperties().put("hibernate.transaction.jta.platform",
        AtomikosJtaPlatform.class.getName());
    vehicleJpaProperties().getProperties().put("javax.persistence.transactionType", "JTA");

    var entityManager = new LocalContainerEntityManagerFactoryBean();
    entityManager.setDataSource(vehicleDatasource());
    entityManager.setJtaDataSource(xaDataSource);
    entityManager.setJpaVendorAdapter(jpaVendorAdapter);
    entityManager.setPackagesToScan(BASE_PACKAGE);
    entityManager.setPersistenceUnitName("vehicle");
    entityManager.setJpaPropertyMap(vehicleJpaProperties().getProperties());
    return entityManager;
  }

}