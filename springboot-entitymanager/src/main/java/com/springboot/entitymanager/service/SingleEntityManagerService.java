package com.springboot.entitymanager.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.persistence.Tuple;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.springboot.entitymanager.dto.DatabaseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
@Transactional
@RequiredArgsConstructor
public class SingleEntityManagerService implements EntityManagerService {

  private final EntityManager entityManager;
  private final ModelMapper modelMapper;

  @Override
  public void findAll() {

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
          return modelMapper.map(map, DatabaseDto.class);
        })
        .toList();

    log.info("{}", cars.toString());
  }

}