package com.springboot.code.example.database.entitymanager.pagination;

import javax.persistence.EntityManager;

public interface EntityManagerPagination {

  EntityManagerPagination query(String query);

  EntityManagerPagination parameter(int position, Object value);

  EntityManagerPagination parameter(String name, Object value);

  EntityManagerPagination ofPageRequest(int page, int size);

  <T> Pagination<T> getPages(Class<T> clazz);

  static EntityManagerPagination create(EntityManager entityManager) {

    return new DefaultEntityManagerPagination(entityManager);
  }

}