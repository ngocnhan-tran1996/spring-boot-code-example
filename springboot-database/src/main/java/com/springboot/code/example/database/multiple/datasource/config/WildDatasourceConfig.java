package com.springboot.code.example.database.multiple.datasource.config;

import java.util.Objects;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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
    basePackages = WildDatasourceConfig.BASE_PACKAGE,
    entityManagerFactoryRef = "wildEntityManager",
    transactionManagerRef = "wildTransactionManager")
public class WildDatasourceConfig {

  public static final String BASE_PACKAGE =
      "com.springboot.code.example.database.multiple.datasource.wild";

  @Bean
  @ConfigurationProperties("app.datasource.wild")
  DataSourceProperties wildDataSourceProperties() {
    return new DataSourceProperties();
  }

  @Bean
  @ConfigurationProperties("app.datasource.wild.configuration")
  HikariDataSource wildDatasource() {
    return wildDataSourceProperties()
        .initializeDataSourceBuilder()
        .type(HikariDataSource.class)
        .build();
  }

  @Bean
  DataSourceInitializer wildDatasourceInitializer() {

    ResourceDatabasePopulator resourceDatabasePopulator = new ResourceDatabasePopulator();
    resourceDatabasePopulator.addScript(new ClassPathResource("multiple-datasource/wild-data.sql"));

    DataSourceInitializer dataSourceInitializer = new DataSourceInitializer();
    dataSourceInitializer.setDataSource(wildDatasource());
    dataSourceInitializer.setDatabasePopulator(resourceDatabasePopulator);
    return dataSourceInitializer;
  }

  @Bean
  @ConfigurationProperties("app.datasource.wild.jpa")
  JpaProperties wildJpaProperties() {
    return new JpaProperties();
  }

  @Bean
  LocalContainerEntityManagerFactoryBean wildEntityManager() {

    EntityManagerFactoryBuilder builder = new EntityManagerFactoryBuilder(
        new HibernateJpaVendorAdapter(),
        wildJpaProperties().getProperties(),
        null);

    return builder
        .dataSource(wildDatasource())
        .packages(BASE_PACKAGE)
        .build();
  }

  @Bean
  PlatformTransactionManager wildTransactionManager() {
    var factory = Objects.requireNonNull(wildEntityManager().getObject());
    return new JpaTransactionManager(factory);
  }

}