package io.ngocnhan_tran1996.code.example.transaction.poly.datasource.config;

import com.atomikos.icatch.jta.UserTransactionImp;
import com.atomikos.icatch.jta.UserTransactionManager;
import jakarta.transaction.SystemException;
import jakarta.transaction.TransactionManager;
import jakarta.transaction.UserTransaction;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.DependsOn;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.jta.JtaTransactionManager;

@TestConfiguration
public class TransactionManagerConfig {

    @Bean(name = "userTransaction")
    UserTransaction userTransaction() throws SystemException {
        var userTransactionImp = new UserTransactionImp();
        userTransactionImp.setTransactionTimeout(1000);
        return userTransactionImp;
    }

    @Bean(name = "atomikosTransactionManager", initMethod = "init", destroyMethod = "close")
    TransactionManager atomikosTransactionManager() {
        var userTransactionManager = new UserTransactionManager();
        userTransactionManager.setForceShutdown(false);
        return userTransactionManager;
    }

    @Bean("jtaTransactionManager")
    @DependsOn({"userTransaction", "atomikosTransactionManager"})
    PlatformTransactionManager transactionManager(
        UserTransaction userTransaction,
        TransactionManager atomikosTransactionManager) throws SystemException {

        return new JtaTransactionManager(userTransaction, atomikosTransactionManager);
    }

}