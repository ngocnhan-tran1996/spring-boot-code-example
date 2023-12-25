package com.springboot.code.example.database.entitymanager;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("entity-manager")
@SpringBootTest
class TypedQueryPaginationTest {

  @Autowired
  TypedQueryPaginationService typedQueryPaginationService;

  @Test
  void testQueryNamePrefix() {

    assertThat(typedQueryPaginationService.queryNamePrefix())
        .hasSize(9);
    assertThat(typedQueryPaginationService.countQueryNamePrefix())
        .isEqualTo(9);
    assertThat(typedQueryPaginationService.extractQueryNamePrefix())
        .isEqualTo(9);
  }

}