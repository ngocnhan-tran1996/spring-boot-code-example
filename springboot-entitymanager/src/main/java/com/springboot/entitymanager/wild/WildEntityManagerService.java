package com.springboot.entitymanager.wild;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Tuple;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.springboot.entitymanager.dto.DatabaseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
@Transactional(transactionManager = "wildTransactionManager")
@RequiredArgsConstructor
public class WildEntityManagerService {

  @PersistenceContext(unitName = "wildEntityManager")
  private final EntityManager wildEntityManager;
  private final ModelMapper modelMapper;

  public void execute() {

    wildEntityManager.createNativeQuery(
        "INSERT INTO {h-schema}ANIMAL(NAME) VALUES ('TEST')")
        .executeUpdate();

    String query = "SELECT id, name FROM {h-schema}ANIMAL";

    @SuppressWarnings("unchecked")
    List<Tuple> animalTuples = wildEntityManager.createNativeQuery(query, Tuple.class)
        .getResultList();

    List<DatabaseDto> animals = animalTuples.stream()
        .map(tuple -> {
          Map<String, Object> map = new HashMap<>();
          tuple.getElements().forEach(element -> map.put(element.getAlias(), tuple.get(element
              .getAlias())));
          return modelMapper.map(map, DatabaseDto.class);
        })
        .toList();

    log.info("{}", animals.toString());
  }

}