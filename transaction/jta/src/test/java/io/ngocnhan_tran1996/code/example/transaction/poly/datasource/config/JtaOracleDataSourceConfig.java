package io.ngocnhan_tran1996.code.example.transaction.poly.datasource.config;

import com.atomikos.jdbc.AtomikosDataSourceBean;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;

@TestConfiguration
@EnableJpaRepositories(
    basePackages = JtaOracleDataSourceConfig.BASE_PACKAGE,
    entityManagerFactoryRef = JtaOracleDataSourceConfig.PERSISTENCE_UNIT,
    transactionManagerRef = "jtaTransactionManager")
public class JtaOracleDataSourceConfig extends AbstractDataSourceConfig {

    public static final String BASE_PACKAGE =
        "io.ngocnhan_tran1996.code.example.transaction.poly.datasource.oracle";
    public static final String PERSISTENCE_UNIT = "jtaOracleEntityManager";

    @Bean(name = "jtaOracleDataSource")
    @ConfigurationProperties("app.datasource.oracle")
    @Override
    public AtomikosDataSourceBean datasource() {

        return new AtomikosDataSourceBean();
    }

    @Bean("oracleJpaProperties")
    @ConfigurationProperties("app.datasource.oracle.jpa")
    @Override
    public JpaProperties jpaProperties() {

        return super.jpaProperties();
    }

    @Bean(name = PERSISTENCE_UNIT)
    @Override
    public LocalContainerEntityManagerFactoryBean entityManager(
        @Qualifier("jtaOracleDataSource") DataSource datasource,
        @Qualifier("oracleJpaProperties") JpaProperties jpaProperties) {

        return super.entityManager(datasource, jpaProperties);
    }

    @Bean("jtaOracleJdbcTemplate")
    JdbcTemplate jdbcTemplate(@Qualifier("jtaOracleDataSource") DataSource datasource) {

        return new JdbcTemplate(datasource);
    }

    @Override
    protected String[] scanPackages() {

        return new String[]{BASE_PACKAGE};
    }

    @Override
    protected String persistenceUnit() {

        return PERSISTENCE_UNIT;
    }

}