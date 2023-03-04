package com.springboot.entitymanager.vehicle;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Tuple;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.springboot.entitymanager.dto.DatabaseDto;
import com.springboot.entitymanager.service.ManagerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Profile("multiple")
@Log4j2
@Service
@Transactional(transactionManager = "vehicleTransactionManager")
@RequiredArgsConstructor
public class VehicleEntityManagerService implements ManagerService {

  @PersistenceContext
  private final EntityManager vehicleEntityManager;
  private final ModelMapper modelMapper;

  @Override
  public void execute() {

    vehicleEntityManager.createNativeQuery(
        "INSERT INTO {h-schema}CAR(NAME) VALUES ('TEST')")
        .executeUpdate();

    String query = "SELECT id, name FROM {h-schema}CAR";

    @SuppressWarnings("unchecked")
    List<Tuple> tuples = vehicleEntityManager.createNativeQuery(query, Tuple.class)
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