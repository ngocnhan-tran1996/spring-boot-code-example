package com.springboot.code.example.database.entitymanager;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import java.util.ArrayList;
import java.util.List;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.persistence.Tuple;
import org.hibernate.jpa.spi.NativeQueryTupleTransformer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.springboot.code.example.database.entitymanager.annotation.EntityManagerConfiguration;

@EntityManagerConfiguration
@ExtendWith(MockitoExtension.class)
class EntityManagerExecutorTest {

  @Mock
  Query query;

  @Mock
  org.hibernate.query.Query<?> hibernateQuery;

  @Mock
  EntityManager entityManager;

  @Spy
  ObjectMapper objectMapper;

  @Spy
  @InjectMocks
  EntityManagerExecutor entityManagerExecutor;

  @Test
  void testSaveAndFindAll() {

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
    entityManagerExecutor.saveAndFindAll();
    verify(entityManagerExecutor, atLeastOnce()).saveAndFindAll();
  }

  @Test
  void testPaginate() {

    // when
    doReturn(query)
        .when(entityManager)
        .createNativeQuery(anyString(), eq(Tuple.class));

    doReturn(hibernateQuery)
        .when(query)
        .unwrap(org.hibernate.query.Query.class);

    doReturn(query)
        .when(entityManager)
        .createNativeQuery(anyString());

    doReturn(20)
        .when(query)
        .getSingleResult();

    // then
    entityManagerExecutor.paginate();
    verify(entityManagerExecutor, atLeastOnce()).paginate();
  }

}