package com.springboot.entitymanager.dto;

import lombok.Setter;

@Setter
public class DatabaseDto {

  private int id;
  private String name;

  @Override
  public String toString() {
    return id + ": " + name;
  }

}
