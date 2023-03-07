package com.springboot.entitymanager.service;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.doReturn;
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

@ExtendWith(MockitoExtension.class)
class SingleEntityManagerServiceTest {

  @Mock
  Query query;

  @Mock
  EntityManager entityManager;

  @Spy
  @InjectMocks
  SingleEntityManagerService singleEntityManagerService;

  @Test
  void testFindAll() {

    doReturn(query)
        .when(entityManager)
        .createNativeQuery(anyString());

    doReturn(query)
        .when(entityManager)
        .createNativeQuery(anyString(), eq(Tuple.class));

    singleEntityManagerService.findAll();
    verify(singleEntityManagerService, atLeastOnce()).findAll();
  }

}