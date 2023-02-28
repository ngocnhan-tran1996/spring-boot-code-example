package com.springboot.multipledatasource.vehicle;

import javax.persistence.Entity;
import javax.persistence.Table;
import com.springboot.multipledatasource.entity.BaseEntity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "CAR")
public class CarEntity extends BaseEntity {

}