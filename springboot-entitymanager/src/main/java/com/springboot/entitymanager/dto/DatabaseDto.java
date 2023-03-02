package com.springboot.entitymanager.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DatabaseDto {

  private int id;
  private String name;

  @Override
  public String toString() {
    return id + ": " + name;
  }

}
