package io.ngocnhan_tran1996.code.example.transaction.poly.datasource.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.jta.JtaTransactionManager;
import com.atomikos.icatch.jta.UserTransactionImp;
import com.atomikos.icatch.jta.UserTransactionManager;
import jakarta.transaction.SystemException;
import jakarta.transaction.TransactionManager;
import jakarta.transaction.UserTransaction;

@TestConfiguration
public class TransactionManagerConfig {

  @Bean
  UserTransaction userTransaction() throws SystemException {
    var userTransactionImp = new UserTransactionImp();
    userTransactionImp.setTransactionTimeout(1000);
    return userTransactionImp;
  }

  @Bean(initMethod = "init", destroyMethod = "close")
  TransactionManager atomikosTransactionManager() {
    var userTransactionManager = new UserTransactionManager();
    userTransactionManager.setForceShutdown(false);
    return userTransactionManager;
  }

  @Bean("jtaTransactionManager")
  PlatformTransactionManager transactionManager(
      UserTransaction userTransaction,
      TransactionManager atomikosTransactionManager) throws SystemException {

    return new JtaTransactionManager(userTransaction, atomikosTransactionManager);
  }

}