package io.ngocnhan_tran1996.code.example.transaction.poly.datasource.config;

import java.util.Objects;
import javax.sql.DataSource;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import com.atomikos.jdbc.AtomikosDataSourceBean;
import com.zaxxer.hikari.HikariDataSource;

abstract class AbstractDataSourceConfig implements DataSourceConfig {

  @Override
  public DataSourceProperties dataSourceProperties() {

    return new DataSourceProperties();
  }

  @Override
  public DataSource datasource(DataSourceProperties dataSourceProperties) {

    return dataSourceProperties
        .initializeDataSourceBuilder()
        .type(HikariDataSource.class)
        .build();
  }

  @Override
  public JpaProperties jpaProperties() {

    return new JpaProperties();
  }

  @Override
  public LocalContainerEntityManagerFactoryBean entityManager(
      DataSource datasource,
      JpaProperties jpaProperties) {

    var builder = new EntityManagerFactoryBuilder(
        new HibernateJpaVendorAdapter(),
        jpaProperties.getProperties(),
        null);

    return builder.dataSource(datasource)
        .packages(this.scanPackages())
        .persistenceUnit(this.persistenceUnit())
        .jta(datasource instanceof AtomikosDataSourceBean)
        .build();
  }

  @Override
  public PlatformTransactionManager transactionManager(
      LocalContainerEntityManagerFactoryBean entityManager) {

    return new JpaTransactionManager(Objects.requireNonNull(entityManager.getObject()));
  }

  protected abstract String[] scanPackages();

  protected abstract String persistenceUnit();

}