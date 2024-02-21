package io.ngocnhan_tran1996.code.example.transaction.poly.datasource.config;

import com.atomikos.jdbc.AtomikosDataSourceBean;
import javax.sql.DataSource;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;

@TestConfiguration
@EnableJpaRepositories(
    basePackages = JtaPostgresDataSourceConfig.BASE_PACKAGE,
    entityManagerFactoryRef = JtaPostgresDataSourceConfig.PERSISTENCE_UNIT,
    transactionManagerRef = "jtaTransactionManager")
public class JtaPostgresDataSourceConfig extends AbstractDataSourceConfig {

    public static final String BASE_PACKAGE =
        "io.ngocnhan_tran1996.code.example.transaction.poly.datasource.postgres";
    public static final String PERSISTENCE_UNIT = "jtaPostgresEntityManager";

    @Primary
    @Bean
    @ConfigurationProperties("app.datasource.postgres")
    @Override
    public AtomikosDataSourceBean datasource() {

        return new AtomikosDataSourceBean();
    }

    @Primary
    @Bean
    @ConfigurationProperties("app.datasource.postgres.jpa")
    @Override
    public JpaProperties jpaProperties() {

        return super.jpaProperties();
    }

    @Primary
    @Bean(PERSISTENCE_UNIT)
    @Override
    public LocalContainerEntityManagerFactoryBean entityManager(
        DataSource datasource,
        JpaProperties jpaProperties) {

        return super.entityManager(datasource, jpaProperties);
    }

    @Bean("jtaPostgresJdbcTemplate")
    JdbcTemplate jdbcTemplate(DataSource datasource) {

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