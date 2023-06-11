package com.springboot.code.example.transaction.multiple.datasource.config;

import java.util.Objects;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
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
      "com.springboot.code.example.transaction.multiple.datasource.vehicle";

  @Primary
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
  LocalContainerEntityManagerFactoryBean vehicleEntityManager() {

    var builder = new EntityManagerFactoryBuilder(
        new HibernateJpaVendorAdapter(),
        vehicleJpaProperties().getProperties(),
        null);

    return builder.dataSource(vehicleDatasource())
        .packages(BASE_PACKAGE)
        .persistenceUnit("vehicle")
        .build();
  }

  @Primary
  @Bean
  PlatformTransactionManager vehicleTransactionManager() {

    return new JpaTransactionManager(Objects.requireNonNull(vehicleEntityManager().getObject()));
  }

}