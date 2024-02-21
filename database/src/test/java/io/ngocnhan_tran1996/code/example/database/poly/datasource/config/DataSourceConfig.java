package io.ngocnhan_tran1996.code.example.database.poly.datasource.config;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

interface DataSourceConfig {

    DataSourceProperties dataSourceProperties();

    HikariDataSource datasource(DataSourceProperties dataSourceProperties);

    JpaProperties jpaProperties();

    LocalContainerEntityManagerFactoryBean entityManager(
        HikariDataSource datasource,
        JpaProperties jpaProperties);

    PlatformTransactionManager transactionManager(
        LocalContainerEntityManagerFactoryBean entityManager);

}