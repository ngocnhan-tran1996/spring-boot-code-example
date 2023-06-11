package com.springboot.code.example.transaction.multiple.datasource.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.data.transaction.ChainedTransactionManager;
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

@SuppressWarnings("deprecation")
@Configuration
public class MultipleDatasourceConfig {

  @Bean
  JpaVendorAdapter jpaVendorAdapter() {

    var hibernateJpaVendorAdapter = new HibernateJpaVendorAdapter();
    hibernateJpaVendorAdapter.setShowSql(true);
    hibernateJpaVendorAdapter.setGenerateDdl(true);
    hibernateJpaVendorAdapter.setDatabase(Database.ORACLE);
    return hibernateJpaVendorAdapter;
  }

  @Bean
  UserTransaction userTransaction() throws SystemException {

    var userTransactionImp = new UserTransactionImp();
    userTransactionImp.setTransactionTimeout(10000);
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

  @Bean(name = "chainedTransactionManager")
  ChainedTransactionManager chainedTransactionManager(
      @Qualifier("vehicleTransactionManager") PlatformTransactionManager vehicleTransactionManager,
      @Qualifier("wildTransactionManager") PlatformTransactionManager wildTransactionManager) {

    return new ChainedTransactionManager(vehicleTransactionManager, wildTransactionManager);
  }

}
