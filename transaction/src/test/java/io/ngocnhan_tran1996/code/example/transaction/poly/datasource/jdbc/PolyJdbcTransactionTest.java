package io.ngocnhan_tran1996.code.example.transaction.poly.datasource.jdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import io.ngocnhan_tran1996.code.example.transaction.poly.datasource.config.OracleDataSourceConfig;
import io.ngocnhan_tran1996.code.example.transaction.poly.datasource.config.PostgresDataSourceConfig;

@ActiveProfiles("poly-datasource")
@SpringBootTest(classes = {PostgresDataSourceConfig.class, OracleDataSourceConfig.class})
class PolyJdbcTransactionTest {

  @Autowired
  JdbcTemplate dogRepo;

  @Autowired
  JdbcTemplate catRepo;



}