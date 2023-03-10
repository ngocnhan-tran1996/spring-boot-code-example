package com.springboot.datasource.common.repository;

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
public class CardEntityManagerRepository {

  private final EntityManagerPagination entityManagerPagination;
  private final ObjectMapper objectMapper;

  public void execute() {

    var e = entityManagerPagination.query("SELECT id, name FROM {h-schema}CAR")
        .ofPageRequest(0, 20)
        .getPages(CarEntity.class);

    // ASTQueryTranslatorFactory queryTranslatorFactory = new ASTQueryTranslatorFactory();
    // SessionImplementor hibernateSession = entityManager.unwrap(NativeQueryImplementor.class);
    // QueryTranslator queryTranslator = queryTranslatorFactory.createQueryTranslator("",
    // hqlQueryString, java.util.Collections.EMPTY_MAP, hibernateSession.getFactory(), null);
    // queryTranslator.compile(java.util.Collections.EMPTY_MAP, false);
    // String sqlQueryString = queryTranslator.getSQLString();

    try {
      log.info("Count is {}", objectMapper.writeValueAsString(e));
    } catch (JsonProcessingException e1) {
      // TODO Auto-generated catch block
      e1.printStackTrace();
    }
  }


}