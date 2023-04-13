package com.springboot.code.example.database.jdbc;

import static org.assertj.core.api.Assertions.assertThat;
import java.util.Map;
import javax.sql.DataSource;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import com.springboot.code.example.database.profiles.Profiles;

@ActiveProfiles(Profiles.JDBC)
@SpringBootTest
class JdbcTemplateExecutorTest {

  @Autowired
  DataSource dataSource;

  @Test
  @Transactional
  void testExecuteProcedure() {

    // given
    var jdbcTemplateExecutor = new JdbcTemplateExecutor(dataSource);

    // when

    // then
    assertThat(jdbcTemplateExecutor.executeProcedure())
        .isEqualTo(Map.of(
            "OUT_MSG",
            "Table already exists"));
  }

}
