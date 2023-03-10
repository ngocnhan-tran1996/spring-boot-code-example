package com.springboot.datasource.common.pagination;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.Tuple;
import org.modelmapper.ModelMapper;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public final class DefaultEntityManagerPagination implements EntityManagerPagination {

  private static final String COUNT_STATEMENT = "SELECT COUNT(*) FROM (%s) count_table";
  private static final ModelMapper MODEL_MAPPER = new ModelMapper();

  private final EntityManager entityManager;
  private Query query;
  private int page;
  private int size;

  public static EntityManagerPagination create(EntityManager entityManager) {
    return new DefaultEntityManagerPagination(entityManager);
  }

  @Override
  public EntityManagerPagination query(String query) {

    this.query = entityManager.createNativeQuery(query, Tuple.class);
    return this;
  }

  @Override
  public EntityManagerPagination parameter(int position, Object value) {

    this.query.setParameter(position, value);
    return this;
  }

  @Override
  public EntityManagerPagination parameter(String name, Object value) {

    this.query.setParameter(name, value);
    return this;
  }

  @Override
  public EntityManagerPagination ofPageRequest(int page, int size) {

    this.page = page;
    this.size = size;
    return this;
  }

  @Override
  public <T> Pagination<T> getPages(Class<T> clazz) {

    if (this.page < 0 || this.size <= 0) {

      return new PaginationImpl<>(this.getResultList(clazz));
    }

    long count = this.count();
    if (count == 0) {

      return new PaginationImpl<>(new ArrayList<>(), this.page, this.size, count);
    }

    this.query.setFirstResult(this.page * this.size);
    this.query.setMaxResults(this.size);
    return new PaginationImpl<>(this.getResultList(clazz), this.page, this.size, count);
  }

  @SuppressWarnings("unchecked")
  private <T> List<T> getResultList(Class<T> clazz) {

    return ((List<Tuple>) this.query.getResultList()).stream()
        .map(tuple -> {
          Map<String, Object> map = new HashMap<>();
          tuple.getElements().forEach(element -> map.put(element.getAlias(), tuple.get(element
              .getAlias())));
          return MODEL_MAPPER.map(map, clazz);
        })
        .toList();
  }

  private long count() {

    var hibernateQuery = query.unwrap(org.hibernate.query.Query.class);
    String queryString = hibernateQuery.getQueryString();
    String countStament = String.format(COUNT_STATEMENT, queryString);
    var countQuery = this.entityManager.createNativeQuery(countStament);
    hibernateQuery.getParameters()
        .forEach(parameter -> {

          var position = parameter.getPosition();
          if (position != null) {
            countQuery.setParameter(position, hibernateQuery.getParameterValue(position));
          }

          var name = parameter.getName();
          if (name != null) {
            countQuery.setParameter(name, hibernateQuery.getParameterValue(name));
          }

        });
    return ((Number) countQuery.getSingleResult()).longValue();
  }

}
