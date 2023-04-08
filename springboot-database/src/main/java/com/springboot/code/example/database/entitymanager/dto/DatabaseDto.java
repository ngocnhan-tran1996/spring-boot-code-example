package com.springboot.code.example.database.entitymanager.dto;

import java.io.Serializable;
import lombok.Setter;

@Setter
public class DatabaseDto implements Serializable {

  private static final long serialVersionUID = 1L;

  private int id;
  private String name;

  @Override
  public String toString() {
    return id + ": " + name;
  }

}