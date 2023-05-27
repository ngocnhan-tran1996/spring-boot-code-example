package com.springboot.code.example.database.multiple.datasource;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CarDto {

  private String id;
  private String name;

  @Override
  public String toString() {
    // TODO Auto-generated method stub
    return id + " " + name;
  }

}