package com.springboot.datasource.common.pagination;

import java.util.List;

public interface Pagination<T> {

  List<T> getElements();

  long getTotalPages();

  long getTotalElements();

  int getPage();

  int getSize();

  boolean isFirst();

  boolean isLast();

  boolean hasNext();

  boolean hasPrevious();
}
