package com.springboot.code.example.database.jdbc;

import static org.assertj.core.api.Assertions.assertThat;
import java.util.Map;
import javax.sql.DataSource;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import com.springboot.code.example.database.jdbc.annotation.JdbcConfiguration;
import com.springboot.code.example.database.jdbc.constant.JdbcConstant;

@JdbcConfiguration
@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
class JdbcTemplateExecutorDatabaseLayerTest {

  private static final Map<String, Object> EXPECT_OUTPUT = Map.of(
      JdbcConstant.EXPECT_OUTPUT_KEY, "Table was created - Table was inserted");

  @Autowired
  DataSource dataSource;

  @Autowired
  JdbcTemplate jdbcTemplate;

  @Test
  @Sql(scripts = "classpath:jdbc/data.sql",
      executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
  void testExecuteProcedure() {

    // given
    var jdbcTemplateExecutor = new JdbcTemplateExecutor(dataSource, jdbcTemplate);

    // then
    assertThat(jdbcTemplateExecutor.executeProcedureWithDataSource())
        .isEqualTo(EXPECT_OUTPUT);
    assertThat(jdbcTemplateExecutor.executeProcedureWithJdbcTemplate())
        .isEqualTo(JdbcTemplateExecutorTest.EXPECT_OUTPUT);
  }

}