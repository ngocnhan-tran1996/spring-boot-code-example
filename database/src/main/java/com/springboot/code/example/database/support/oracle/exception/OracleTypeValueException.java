package com.springboot.code.example.database.support.oracle.exception;

public class OracleTypeValueException extends RuntimeException {

  private static final long serialVersionUID = 1L;

  public OracleTypeValueException(String msg) {
    super(msg);
  }

}