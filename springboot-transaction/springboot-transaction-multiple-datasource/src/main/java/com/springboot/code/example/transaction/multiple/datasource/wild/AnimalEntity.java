package com.springboot.code.example.transaction.multiple.datasource.wild;

import javax.persistence.Entity;
import javax.persistence.Table;
import com.springboot.code.example.transaction.multiple.datasource.entity.BaseEntity;
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