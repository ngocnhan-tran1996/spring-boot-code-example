package com.springboot.code.example.database.jdbc;

import static org.assertj.core.api.Assertions.assertThat;
import java.math.BigDecimal;
import java.util.Map;
import javax.sql.DataSource;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.jdbc.core.JdbcTemplate;
import com.springboot.code.example.database.jdbc.annotation.JdbcConfiguration;
import com.springboot.code.example.database.jdbc.constant.JdbcConstant;
import com.springboot.code.example.database.jdbc.oracle.dto.OracleJdbcTemplateDto.PersonOuput;

@JdbcConfiguration
@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
class OracleJdbcTemplateExecutorDatabaseLayerTest {

  @Autowired
  DataSource dataSource;

  @Autowired
  JdbcTemplate jdbcTemplate;

  @Test
  void testExecuteProcedure() {

    // given
    var outMsg = "IN_NAME = 1 AND IN_PARAMS =  NAME: %s AND AGE: %s AND  NAME: %s AND AGE: %s";
    var jdbcTemplateExecutor = new OracleJdbcTemplateExecutor(jdbcTemplate);
    var personOutput = new PersonOuput();
    personOutput.setOutMsg(String.format(outMsg, "Harry", "15", "Judy", "16"));
    personOutput.setNumber(BigDecimal.valueOf(2010));

    // then
    assertThat(jdbcTemplateExecutor.executeProcedureWithOracleArrayValue())
        .usingRecursiveComparison()
        .isEqualTo(personOutput);

    assertThat(jdbcTemplateExecutor.executeProcedureWithSQLData())
        .usingRecursiveComparison()
        .isEqualTo(Map.of(
            JdbcConstant.EXPECT_OUTPUT_KEY, String.format(outMsg, "Paul", "12", "Victor", "13"),
            "OUT_NBR", BigDecimal.valueOf(2010)));
  }

}