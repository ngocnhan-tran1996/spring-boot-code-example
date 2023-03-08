package com.springboot.entitymanager.vehicle;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.Tuple;
import org.hibernate.jpa.spi.NativeQueryTupleTransformer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

@ExtendWith(MockitoExtension.class)
class VehicleEntityManagerServiceTest {

  @Mock
  Query query;

  @Mock
  EntityManager entityManager;

  @Spy
  ModelMapper modelMapper;

  @Spy
  @InjectMocks
  VehicleEntityManagerService vehicleEntityManagerService;

  @Test
  void testExecute() {

    // given
    var alias = new String[] {"ID", "NAME"};
    List<Tuple> tuples = new ArrayList<>();
    NativeQueryTupleTransformer nativeQueryTupleTransformer = new NativeQueryTupleTransformer();

    tuples.add((Tuple) nativeQueryTupleTransformer.transformTuple(
        new Object[] {1, "TOYOTA"},
        alias));
    tuples.add((Tuple) nativeQueryTupleTransformer.transformTuple(
        new Object[] {2, "FORD"},
        alias));

    // when
    doReturn(query)
        .when(entityManager)
        .createNativeQuery(anyString());

    doReturn(query)
        .when(entityManager)
        .createNativeQuery(anyString(), eq(Tuple.class));

    doReturn(tuples)
        .when(query)
        .getResultList();

    // then
    vehicleEntityManagerService.execute();
    verify(vehicleEntityManagerService, atLeastOnce()).execute();
  }

}