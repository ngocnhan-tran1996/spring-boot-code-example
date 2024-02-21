package io.ngocnhan_tran1996.code.example.transaction.poly.datasource.config;

import javax.sql.DataSource;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;

interface DataSourceConfig {

    DataSource datasource();

    JpaProperties jpaProperties();

    LocalContainerEntityManagerFactoryBean entityManager(
        DataSource datasource,
        JpaProperties jpaProperties);
}