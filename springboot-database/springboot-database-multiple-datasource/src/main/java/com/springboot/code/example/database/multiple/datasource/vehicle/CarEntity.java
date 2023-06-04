package com.springboot.code.example.database.multiple.datasource.vehicle;

import com.springboot.code.example.database.multiple.datasource.entity.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
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