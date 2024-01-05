package io.ngocnhan_tran1996.code.example.transaction.poly.datasource.config;

import javax.sql.DataSource;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

abstract class AbstractDataSourceConfig implements DataSourceConfig {

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
        .jta(true)
        .build();
  }

  protected abstract String[] scanPackages();

  protected abstract String persistenceUnit();

}