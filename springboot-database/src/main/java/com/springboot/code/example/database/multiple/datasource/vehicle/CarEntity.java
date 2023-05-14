package com.springboot.code.example.database.multiple.datasource.vehicle;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import com.springboot.code.example.database.multiple.datasource.entity.BaseEntity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "CAR")
public class CarEntity extends BaseEntity {

}