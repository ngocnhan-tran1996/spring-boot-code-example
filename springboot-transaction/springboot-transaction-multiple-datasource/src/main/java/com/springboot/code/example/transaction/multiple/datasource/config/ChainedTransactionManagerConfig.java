package com.springboot.code.example.transaction.multiple.datasource.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.transaction.ChainedTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

@SuppressWarnings("deprecation")
@Configuration
public class ChainedTransactionManagerConfig {

  @Bean(name = "chainedTransactionManager")
  ChainedTransactionManager chainedTransactionManager(
      @Qualifier("vehicleTransactionManager") PlatformTransactionManager vehicleTransactionManager,
      @Qualifier("wildTransactionManager") PlatformTransactionManager wildTransactionManager) {

    return new ChainedTransactionManager(vehicleTransactionManager, wildTransactionManager);
  }

}
