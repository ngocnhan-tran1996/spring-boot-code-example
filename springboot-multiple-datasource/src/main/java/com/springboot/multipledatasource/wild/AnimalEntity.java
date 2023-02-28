package com.springboot.multipledatasource.wild;

import javax.persistence.Entity;
import javax.persistence.Table;
import com.springboot.multipledatasource.entity.BaseEntity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "ANIMAL")
public class AnimalEntity extends BaseEntity {

}