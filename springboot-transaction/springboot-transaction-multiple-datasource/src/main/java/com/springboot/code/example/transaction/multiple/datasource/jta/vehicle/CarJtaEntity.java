package com.springboot.code.example.transaction.multiple.datasource.jta.vehicle;

import javax.persistence.Entity;
import javax.persistence.Table;
import com.springboot.code.example.transaction.multiple.datasource.entity.BaseEntity;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Entity
@Table(name = "CAR_JTA")
public class CarJtaEntity extends BaseEntity {

  public CarJtaEntity(int id, String name) {
    super.setId(id);
    super.setName(name);
  }

}