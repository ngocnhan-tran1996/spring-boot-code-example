package com.springboot.datasource.common.service;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.springboot.datasource.common.entity.CarEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
@Transactional
@RequiredArgsConstructor
public class CardJdbcTemplateService {

  private final JdbcTemplate jdbcTemplate;
  private final ObjectMapper objectMapper;

  private final RowMapper<CarEntity> carRowMapper = (resultSet, rowNum) -> {

    var car = new CarEntity();
    car.setId(resultSet.getInt("id"));
    car.setName(resultSet.getString("name"));
    return car;
  };


  public void execute() {

    try {
      var cars = jdbcTemplate.query("SELECT id, name FROM CAR", carRowMapper);
      var count = new SimpleJdbcCall(jdbcTemplate)
          // .withSchemaName(dataSource.getSchema())
          .withFunctionName("COUNT_CAR")
          .executeFunction(Integer.class);
      log.info("Count: {} - Cars: {}", count, objectMapper.writeValueAsString(cars));
    } catch (JsonProcessingException e1) {
      // nothing to do
    }
  }

}