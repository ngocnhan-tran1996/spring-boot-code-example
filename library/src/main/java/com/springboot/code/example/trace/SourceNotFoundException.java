package com.springboot.code.example.trace;

class SourceNotFoundException extends RuntimeException {

  private static final long serialVersionUID = 1L;

  SourceNotFoundException(String message) {

    super(message);
  }

}