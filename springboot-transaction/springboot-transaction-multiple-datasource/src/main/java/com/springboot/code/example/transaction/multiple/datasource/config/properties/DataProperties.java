package com.springboot.code.example.transaction.multiple.datasource.config.properties;

import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import com.zaxxer.hikari.HikariDataSource;

@Configuration
public class DataProperties {

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

  @Bean
  @ConfigurationProperties("app.datasource.wild")
  DataSourceProperties wildDataSourceProperties() {

    return new DataSourceProperties();
  }

  @Bean
  @ConfigurationProperties("app.datasource.wild.hikari")
  HikariDataSource wildDatasource() {

    return wildDataSourceProperties()
        .initializeDataSourceBuilder()
        .type(HikariDataSource.class)
        .build();
  }

  @Bean
  @ConfigurationProperties("app.datasource.wild.jpa")
  JpaProperties wildJpaProperties() {
    return new JpaProperties();
  }

}
