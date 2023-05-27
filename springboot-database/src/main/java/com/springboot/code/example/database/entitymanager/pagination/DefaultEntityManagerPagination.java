package com.springboot.code.example.database.entitymanager.pagination;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import com.springboot.code.example.database.converter.PropertyConverter;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.persistence.Tuple;
import jakarta.persistence.criteria.CriteriaQuery;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public final class DefaultEntityManagerPagination implements EntityManagerPagination {

  private static final String COUNT_STATEMENT = "SELECT COUNT(1) FROM (%s) count_table";

  private final EntityManager entityManager;
  private Query query;
  private int page;
  private int size;

  @Override
  public EntityManagerPagination query(String query) {

    this.query = entityManager.createNativeQuery(query, Tuple.class);
    return this;
  }

  @Override
  public <T> EntityManagerPagination query(CriteriaQuery<T> criteriaQuery) {

    this.query = entityManager.createQuery(criteriaQuery);
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
    if (count == 0
        || Pagination.calculateTotalPages(count, size) < this.page + 1) {

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
          return PropertyConverter.convert(map, clazz);
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

          Optional.ofNullable(parameter.getPosition())
              .ifPresent(position -> countQuery.setParameter(
                  position,
                  hibernateQuery.getParameterValue(position)));

          Optional.ofNullable(parameter.getName())
              .ifPresent(name -> countQuery.setParameter(
                  name,
                  hibernateQuery.getParameterValue(name)));

        });
    return ((Number) countQuery.getSingleResult()).longValue();
  }

}
