package com.springboot.code.example.transaction.jdbc.example;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class JdbcExample {

  private final JdbcTemplate jdbcTemplate;

  @Transactional
  public void insert() {

    jdbcTemplate.execute("INSERT INTO ANIMATION (NAME) VALUES ('NAME')");
  }

}