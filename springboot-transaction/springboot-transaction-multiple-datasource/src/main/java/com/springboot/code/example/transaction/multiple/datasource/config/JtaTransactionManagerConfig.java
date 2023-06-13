package com.springboot.code.example.transaction.multiple.datasource.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.jta.JtaTransactionManager;
import com.atomikos.icatch.jta.UserTransactionImp;
import com.atomikos.icatch.jta.UserTransactionManager;
import jakarta.transaction.SystemException;
import jakarta.transaction.TransactionManager;
import jakarta.transaction.UserTransaction;

@Configuration
public class JtaTransactionManagerConfig {

  @Bean
  UserTransaction userTransaction() throws SystemException {

    var userTransactionImp = new UserTransactionImp();
    userTransactionImp.setTransactionTimeout(300000);
    return userTransactionImp;
  }

  @Bean(initMethod = "init", destroyMethod = "close")
  TransactionManager atomikosTransactionManager() {

    var userTransactionManager = new UserTransactionManager();
    userTransactionManager.setForceShutdown(false);
    return userTransactionManager;
  }

  @Bean
  @DependsOn({"userTransaction", "atomikosTransactionManager"})
  PlatformTransactionManager transactionManager() throws SystemException {

    return new JtaTransactionManager(userTransaction(), atomikosTransactionManager());
  }

}
