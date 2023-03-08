package com.springboot.entitymanager.service;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.lenient;
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
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.transaction.support.TransactionTemplate;

@ExtendWith(MockitoExtension.class)
class MultipleEntityManagerWithoutAnnotationServiceTest {

  @Mock
  Query vehicleQuery;

  @Mock
  Query wildQuery;

  @Mock
  TransactionTemplate transactionTemplate;

  @Mock
  EntityManager vehicleEntityManager;

  @Mock
  EntityManager wildEntityManager;

  @Spy
  ModelMapper modelMapper;

  @Spy
  @InjectMocks
  MultipleEntityManagerWithoutAnnotationService multipleEntityManagerWithoutAnnotationService;

  @Test
  void testFindAll() {

    // given
    ReflectionTestUtils.setField(multipleEntityManagerWithoutAnnotationService,
        "vehicleEntityManager",
        vehicleEntityManager);
    ReflectionTestUtils.setField(multipleEntityManagerWithoutAnnotationService,
        "wildEntityManager",
        wildEntityManager);

    var alias = new String[] {"ID", "NAME"};
    NativeQueryTupleTransformer nativeQueryTupleTransformer = new NativeQueryTupleTransformer();

    List<Tuple> vehicleTuples = new ArrayList<>();
    vehicleTuples.add((Tuple) nativeQueryTupleTransformer.transformTuple(
        new Object[] {1, "TOYOTA"},
        alias));
    vehicleTuples.add((Tuple) nativeQueryTupleTransformer.transformTuple(
        new Object[] {2, "FORD"},
        alias));

    List<Tuple> wildTuples = new ArrayList<>();
    wildTuples.add((Tuple) nativeQueryTupleTransformer.transformTuple(
        new Object[] {1, "LION"},
        alias));
    wildTuples.add((Tuple) nativeQueryTupleTransformer.transformTuple(
        new Object[] {2, "CAT"},
        alias));

    // when
    lenient()
        .doReturn(vehicleQuery)
        .when(vehicleEntityManager)
        .createNativeQuery(anyString(), eq(Tuple.class));

    lenient()
        .doReturn(vehicleTuples)
        .when(vehicleQuery)
        .getResultList();

    doReturn(wildQuery)
        .when(wildEntityManager)
        .createNativeQuery(anyString(), eq(Tuple.class));

    doReturn(wildTuples)
        .when(wildQuery)
        .getResultList();

    // then
    multipleEntityManagerWithoutAnnotationService.findAll();
    verify(multipleEntityManagerWithoutAnnotationService, atLeastOnce()).findAll();
  }

}