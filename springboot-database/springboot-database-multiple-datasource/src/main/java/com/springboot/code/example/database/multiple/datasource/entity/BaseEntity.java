package com.springboot.code.example.database.multiple.datasource.entity;

import com.springboot.code.example.common.helper.Strings;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@MappedSuperclass
public abstract class BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  private String name;

  @Override
  public String toString() {

    return this.getId() + Strings.SPACE + this.getName();
  }

}