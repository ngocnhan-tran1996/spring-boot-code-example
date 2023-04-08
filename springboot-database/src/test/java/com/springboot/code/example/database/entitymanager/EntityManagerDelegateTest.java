package com.springboot.code.example.database.entitymanager;

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
import org.springframework.test.context.ActiveProfiles;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.springboot.code.example.database.profiles.Profiles;

@ActiveProfiles(profiles = Profiles.ENTITY_MANAGER)
@ExtendWith(MockitoExtension.class)
class EntityManagerDelegateTest {

  @Mock
  Query query;

  @Mock
  org.hibernate.query.Query<?> hibernateQuery;

  @Mock
  EntityManager entityManager;

  @Spy
  ModelMapper modelMapper;

  @Spy
  ObjectMapper objectMapper;

  @Spy
  @InjectMocks
  EntityManagerDelegate entityManagerDelegate;

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
    entityManagerDelegate.saveAndFindAll();
    verify(entityManagerDelegate, atLeastOnce()).saveAndFindAll();
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
    entityManagerDelegate.paginate();
    verify(entityManagerDelegate, atLeastOnce()).paginate();
  }

}
