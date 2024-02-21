package io.ngocnhan_tran1996.code.example.transaction.poly.datasource.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.transaction.ChainedTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

@TestConfiguration
public class ChainedTransactionManagerConfig {

    @Bean(name = "chainedTransactionManager")
    ChainedTransactionManager chainedTransactionManager(
        @Qualifier(OracleDataSourceConfig.TRANSACTION_MANAGER) PlatformTransactionManager oracleTransactionManager,
        @Qualifier(PostgresDataSourceConfig.TRANSACTION_MANAGER) PlatformTransactionManager postgresTransactionManager) {

        return new ChainedTransactionManager(postgresTransactionManager, oracleTransactionManager);
    }

}