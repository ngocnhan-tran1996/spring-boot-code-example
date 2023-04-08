package com.springboot.code.example.database.entitymanager.pagination;

import java.util.List;

public interface Pagination<T> {

  List<T> getElements();

  long getTotalPages();

  long getTotalElements();

  int getNumberOfElements();

  int getPage();

  int getSize();

  boolean isFirst();

  boolean isLast();

  boolean hasNext();

  boolean hasPrevious();

  static long calculateTotalPages(long count, int size) {

    return size == 0
        ? count
        : (long) Math.ceil((double) count / size);
  }

}
