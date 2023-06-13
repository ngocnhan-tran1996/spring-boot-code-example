package com.springboot.code.example.transaction.multiple.datasource.config.jta;

import java.sql.SQLException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
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

  @Primary
  @Bean
  LocalContainerEntityManagerFactoryBean vehicleJtaEntityManager(
      @Qualifier("vehicleDataSourceProperties") DataSourceProperties vehicleDataSourceProperties,
      @Qualifier("vehicleDatasource") HikariDataSource vehicleDatasource,
      @Qualifier("vehicleJpaProperties") JpaProperties vehicleJpaProperties)
      throws SQLException {

    var datasource = new OracleXADataSource();
    datasource.setURL(vehicleDataSourceProperties.getUrl());
    datasource.setUser(vehicleDataSourceProperties.getUsername());
    datasource.setPassword(vehicleDataSourceProperties.getPassword());

    var xaDataSource = new AtomikosDataSourceBean();
    xaDataSource.setXaDataSource(datasource);
    xaDataSource.setUniqueResourceName("vehicle");
    xaDataSource.setMinPoolSize(vehicleDatasource.getMinimumIdle());
    xaDataSource.setMaxPoolSize(vehicleDatasource.getMaximumPoolSize());

    var entityManager = new LocalContainerEntityManagerFactoryBean();
    entityManager.setJtaDataSource(xaDataSource);
    entityManager.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
    entityManager.setPackagesToScan(BASE_PACKAGE);
    entityManager.setPersistenceUnitName("vehicle_jta");
    entityManager.setJpaPropertyMap(vehicleJpaProperties.getProperties());
    return entityManager;
  }

}