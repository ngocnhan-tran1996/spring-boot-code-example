package com.springboot.entitymanager.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Tuple;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;
import com.springboot.entitymanager.dto.DatabaseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Profile("multiple")
@Log4j2
@Service
@RequiredArgsConstructor
public class MultipleEntityManagerWithoutAnnotationService implements EntityManagerService {

  private final TransactionTemplate vehicleTransactionTemplate;
  private final TransactionTemplate wildTransactionTemplate;
  @PersistenceContext
  private final EntityManager vehicleEntityManager;
  @PersistenceContext(unitName = "wildEntityManager")
  private final EntityManager wildEntityManager;
  private final ModelMapper modelMapper;

  @Override
  public void findAll() {

    vehicleTransactionTemplate.executeWithoutResult(status -> vehicleEntityManager
        .createNativeQuery("INSERT INTO {h-schema}CAR(NAME) VALUES ('TEST')")
        .executeUpdate());

    String carQuery = "SELECT id, name FROM {h-schema}CAR";

    @SuppressWarnings("unchecked")
    List<Tuple> tuples = vehicleEntityManager.createNativeQuery(carQuery, Tuple.class)
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

    wildTransactionTemplate.executeWithoutResult(status -> wildEntityManager
        .createNativeQuery("INSERT INTO {h-schema}ANIMAL(NAME) VALUES ('TEST')")
        .executeUpdate());

    String wildQuery = "SELECT id, name FROM {h-schema}ANIMAL";

    @SuppressWarnings("unchecked")
    List<Tuple> animalTuples = wildEntityManager.createNativeQuery(wildQuery, Tuple.class)
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