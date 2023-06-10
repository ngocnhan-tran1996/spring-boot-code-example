package com.springboot.code.example.database.multiple.datasource.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Primary;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.jta.JtaTransactionManager;
import com.atomikos.icatch.jta.UserTransactionImp;
import com.atomikos.icatch.jta.UserTransactionManager;
import jakarta.transaction.SystemException;
import jakarta.transaction.TransactionManager;
import jakarta.transaction.UserTransaction;

@Configuration
public class AtomikosConfig {

  @Bean
  JpaVendorAdapter jpaVendorAdapter() {

    var hibernateJpaVendorAdapter = new HibernateJpaVendorAdapter();
    hibernateJpaVendorAdapter.setShowSql(true);
    hibernateJpaVendorAdapter.setGenerateDdl(true);
    hibernateJpaVendorAdapter.setDatabase(Database.ORACLE);
    return hibernateJpaVendorAdapter;
  }

  @Bean(name = "userTransaction")
  UserTransaction userTransaction() throws SystemException {

    var userTransactionImp = new UserTransactionImp();
    userTransactionImp.setTransactionTimeout(10000);
    return userTransactionImp;
  }

  @Bean(name = "atomikosTransactionManager", initMethod = "init", destroyMethod = "close")
  TransactionManager atomikosTransactionManager() {

    var userTransactionManager = new UserTransactionManager();
    userTransactionManager.setForceShutdown(false);
    return userTransactionManager;
  }

  @Primary
  @Bean(name = "transactionManager")
  @DependsOn({"userTransaction", "atomikosTransactionManager"})
  PlatformTransactionManager transactionManager() throws SystemException {

    return new JtaTransactionManager(userTransaction(), atomikosTransactionManager());
  }

}