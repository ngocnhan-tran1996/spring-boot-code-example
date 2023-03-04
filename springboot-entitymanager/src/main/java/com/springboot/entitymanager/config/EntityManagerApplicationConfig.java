package com.springboot.entitymanager.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

@Configuration
public class EntityManagerApplicationConfig {

  @Bean
  ModelMapper modelMapper() {
    return new ModelMapper();
  }

  @Profile("multiple")
  @Bean
  TransactionTemplate vehicleTransactionTemplate(
      PlatformTransactionManager vehicleTransactionManager) {
    return new TransactionTemplate(vehicleTransactionManager);
  }

  @Profile("multiple")
  @Bean
  TransactionTemplate wildTransactionTemplate(
      PlatformTransactionManager wildTransactionManager) {
    return new TransactionTemplate(wildTransactionManager);
  }

}
