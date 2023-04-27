package com.springboot.code.example.database.entitymanager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.persistence.Tuple;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.springboot.code.example.database.converter.PropertyConverter;
import com.springboot.code.example.database.entitymanager.dto.DatabaseDto;
import com.springboot.code.example.database.entitymanager.pagination.EntityManagerPagination;
import com.springboot.code.example.database.multiple.datasource.vehicle.CarEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
@RequiredArgsConstructor
public class EntityManagerExecutor {

  private final EntityManager entityManager;
  private final ObjectMapper objectMapper;

  // native query
  public void saveAndFindAll() {

    entityManager.createNativeQuery(
        "INSERT INTO {h-schema}CAR(NAME) VALUES ('TEST')")
        .executeUpdate();

    String query = "SELECT id, name FROM {h-schema}CAR";

    @SuppressWarnings("unchecked")
    List<Tuple> tuples = entityManager.createNativeQuery(query, Tuple.class)
        .getResultList();

    List<DatabaseDto> cars = tuples.stream()
        .map(tuple -> {
          Map<String, Object> map = new HashMap<>();
          tuple.getElements().forEach(element -> map.put(element.getAlias(), tuple.get(element
              .getAlias())));
          return PropertyConverter.convert(map, DatabaseDto.class);
        })
        .toList();

    log.info("{}", cars.toString());
  }

  // pagination query
  public void paginate() {

    select(0, 0);
    select(0, 20);
    select(0, 2);
    select(1, 2);
    select(2, 2);
    select(3, 2);
  }

  protected void select(int page, int size) {

    var entityManagerPagination = EntityManagerPagination.create(entityManager);
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
