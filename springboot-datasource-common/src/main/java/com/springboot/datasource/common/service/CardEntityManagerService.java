package com.springboot.datasource.common.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.springboot.datasource.common.entity.CarEntity;
import com.springboot.datasource.common.pagination.EntityManagerPagination;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
@Transactional
@RequiredArgsConstructor
public class CardEntityManagerService {

  private final EntityManagerPagination entityManagerPagination;
  private static final ObjectMapper objectMapper = new ObjectMapper();

  public void execute() {

    select(0, 0);
    select(0, 20);
    select(0, 2);
    select(1, 2);
    select(2, 2);
    select(3, 2);
  }

  protected void select(int page, int size) {

    try {
      var cars = entityManagerPagination.query("SELECT id, name FROM {h-schema}CAR")
          .ofPageRequest(page, size)
          .getPages(CarEntity.class);
      log.info("Page: {} and size: {}, cars: {}",
          page,
          size,
          objectMapper.writeValueAsString(cars));
    } catch (JsonProcessingException e1) {
      // nothing to do
    }
  }

}