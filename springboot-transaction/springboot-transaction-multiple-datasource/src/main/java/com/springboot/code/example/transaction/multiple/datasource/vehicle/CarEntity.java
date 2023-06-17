package com.springboot.code.example.transaction.multiple.datasource.vehicle;

import javax.persistence.Entity;
import javax.persistence.Table;
import com.springboot.code.example.transaction.multiple.datasource.entity.BaseEntity;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Entity
@Table(name = "CAR")
public class CarEntity extends BaseEntity {

  public CarEntity(int id, String name) {
    super.setId(id);
    super.setName(name);
  }

}