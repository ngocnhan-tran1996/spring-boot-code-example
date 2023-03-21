package com.springboot.datasource.common.config;

import javax.persistence.EntityManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.springboot.datasource.common.pagination.DefaultEntityManagerPagination;
import com.springboot.datasource.common.pagination.EntityManagerPagination;

@Configuration
public class DatasourceCommonConfig {

  @Bean
  EntityManagerPagination entityManagerPagination(EntityManager entityManager) {

    return DefaultEntityManagerPagination.create(entityManager);
  }

  @Bean
  ObjectMapper objectMapper() {

    return new ObjectMapper();
  }

}
