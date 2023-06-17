package com.springboot.code.example.transaction.multiple.datasource.jta.wild;

import javax.persistence.Entity;
import javax.persistence.Table;
import com.springboot.code.example.transaction.multiple.datasource.entity.BaseEntity;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Entity
@Table(name = "ANIMAL_JTA")
public class AnimalJtaEntity extends BaseEntity {

  public AnimalJtaEntity(int id, String name) {
    super.setId(id);
    super.setName(name);
  }

}