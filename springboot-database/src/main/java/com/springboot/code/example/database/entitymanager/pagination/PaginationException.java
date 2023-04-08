package com.springboot.code.example.database.entitymanager.pagination;

public class PaginationException extends IllegalArgumentException {

  private static final long serialVersionUID = 1L;

  public PaginationException(String message) {

    super(String.format("%s cannot be negative", message));
  }

}
