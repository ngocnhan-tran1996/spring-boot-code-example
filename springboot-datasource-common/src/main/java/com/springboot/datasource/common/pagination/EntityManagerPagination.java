package com.springboot.datasource.common.pagination;

public interface EntityManagerPagination {

  EntityManagerPagination query(String query);

  EntityManagerPagination parameter(int position, Object value);

  EntityManagerPagination parameter(String name, Object value);

  EntityManagerPagination ofPageRequest(int page, int size);

  <T> Pagination<T> getPages(Class<T> clazz);

}