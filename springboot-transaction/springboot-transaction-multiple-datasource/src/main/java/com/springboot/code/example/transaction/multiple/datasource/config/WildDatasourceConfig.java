package com.springboot.code.example.transaction.multiple.datasource.config;

import java.util.Objects;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
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
      "com.springboot.code.example.transaction.multiple.datasource.wild";

  @Bean
  LocalContainerEntityManagerFactoryBean wildEntityManager(
      @Qualifier("wildDatasource") HikariDataSource wildDatasource,
      @Qualifier("wildJpaProperties") JpaProperties wildJpaProperties) {

    var builder = new EntityManagerFactoryBuilder(
        new HibernateJpaVendorAdapter(),
        wildJpaProperties.getProperties(),
        null);

    return builder.dataSource(wildDatasource)
        .packages(BASE_PACKAGE)
        .persistenceUnit("wild")
        .build();
  }

  @Bean
  PlatformTransactionManager wildTransactionManager(
      @Qualifier("wildEntityManager") LocalContainerEntityManagerFactoryBean wildEntityManager) {

    return new JpaTransactionManager(Objects.requireNonNull(wildEntityManager.getObject()));
  }

}