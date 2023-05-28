package com.springboot.code.example.database.jdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import com.springboot.code.example.database.jdbc.example.JdbcComplexTypeExample;
import lombok.extern.log4j.Log4j2;

@Log4j2
@SpringBootApplication
public class JdbcApplication {

  public static void main(String[] args) {
    SpringApplication.run(JdbcApplication.class, args);
  }

  @Autowired
  JdbcComplexTypeExample jdbcComplexTypeExample;

  @Bean
  CommandLineRunner commandLineRunner() {
    return args -> {
      log.info("query {}", jdbcComplexTypeExample.executeFunctionWithInOutParameter());
      log.info("query {}", jdbcComplexTypeExample.executeFunctionWithRowMapper());
      log.info("query {}", jdbcComplexTypeExample.executeFunctionWithTable());
      log.info("query {}", jdbcComplexTypeExample.executeProcedureWithObject());
      log.info("query {}", jdbcComplexTypeExample.executeProcedureWithOracleArrayValue());
      log.info("query {}", jdbcComplexTypeExample.executeProcedureWithSQLData());
    };
  }

}