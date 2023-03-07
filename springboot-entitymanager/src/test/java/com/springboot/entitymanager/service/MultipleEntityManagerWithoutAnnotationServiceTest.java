package com.springboot.entitymanager.service;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.verify;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.Tuple;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.transaction.support.TransactionTemplate;

@ExtendWith(MockitoExtension.class)
class MultipleEntityManagerWithoutAnnotationServiceTest {

  @Mock
  Query query;

  @Mock
  TransactionTemplate vehicleTransactionTemplate;

  @Mock
  TransactionTemplate wildTransactionTemplate;

  @Mock
  EntityManager vehicleEntityManager;

  @Mock
  EntityManager wildEntityManager;

  @Spy
  @InjectMocks
  MultipleEntityManagerWithoutAnnotationService multipleEntityManagerWithoutAnnotationService;

  @Test
  void testFindAll() {

    lenient()
        .doReturn(query)
        .when(vehicleEntityManager)
        .createNativeQuery(anyString(), eq(Tuple.class));

    lenient()
        .doReturn(query)
        .when(wildEntityManager)
        .createNativeQuery(anyString(), eq(Tuple.class));

    multipleEntityManagerWithoutAnnotationService.findAll();
    verify(multipleEntityManagerWithoutAnnotationService, atLeastOnce()).findAll();
  }

}