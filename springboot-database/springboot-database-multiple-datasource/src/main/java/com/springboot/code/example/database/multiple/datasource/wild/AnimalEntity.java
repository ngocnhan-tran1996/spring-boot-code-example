package com.springboot.code.example.database.multiple.datasource.wild;

import com.springboot.code.example.database.multiple.datasource.entity.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Entity
@Table(name = "ANIMAL")
public class AnimalEntity extends BaseEntity {

  public AnimalEntity(int id, String name) {
    super.setId(id);
    super.setName(name);
  }

}