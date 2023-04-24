package com.springboot.code.example.database.multiple.datasource.config;

import java.util.Objects;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.init.DataSourceInitializer;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import com.zaxxer.hikari.HikariDataSource;

@Configuration
@EnableJpaRepositories(
    basePackages = VehicleDatasourceConfig.BASE_PACKAGE,
    entityManagerFactoryRef = "vehicleEntityManager",
    transactionManagerRef = "vehicleTransactionManager")
public class VehicleDatasourceConfig {

  public static final String BASE_PACKAGE =
      "com.springboot.code.example.database.multiple.datasource.vehicle";

  @Primary
  @Bean
  @ConfigurationProperties("app.datasource.vehicle")
  DataSourceProperties vehicleDataSourceProperties() {
    return new DataSourceProperties();
  }

  @Primary
  @Bean
  @ConfigurationProperties("app.datasource.vehicle.configuration")
  HikariDataSource vehicleDatasource() {
    return vehicleDataSourceProperties()
        .initializeDataSourceBuilder()
        .type(HikariDataSource.class)
        .build();
  }

  @Primary
  @Bean
  DataSourceInitializer vehicleDatasourceInitializer() {

    ResourceDatabasePopulator resourceDatabasePopulator = new ResourceDatabasePopulator();
    resourceDatabasePopulator.addScript(new ClassPathResource(
        "multiple-datasource/vehicle-data.sql"));

    DataSourceInitializer dataSourceInitializer = new DataSourceInitializer();
    dataSourceInitializer.setDataSource(vehicleDatasource());
    dataSourceInitializer.setDatabasePopulator(resourceDatabasePopulator);
    return dataSourceInitializer;
  }

  @Primary
  @Bean
  @ConfigurationProperties("app.datasource.vehicle.jpa")
  JpaProperties vehicleJpaProperties() {
    return new JpaProperties();
  }

  @Primary
  @Bean
  LocalContainerEntityManagerFactoryBean vehicleEntityManager() {

    EntityManagerFactoryBuilder builder = new EntityManagerFactoryBuilder(
        new HibernateJpaVendorAdapter(),
        vehicleJpaProperties().getProperties(),
        null);

    return builder
        .dataSource(vehicleDatasource())
        .packages(BASE_PACKAGE)
        .build();
  }

  @Primary
  @Bean
  PlatformTransactionManager vehicleTransactionManager() {
    var factory = Objects.requireNonNull(vehicleEntityManager().getObject());
    return new JpaTransactionManager(factory);
  }

}