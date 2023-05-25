package com.springboot.code.example.database.multiple.datasource.wild;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import com.springboot.code.example.database.multiple.datasource.entity.BaseEntity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "ANIMAL")
public class AnimalEntity extends BaseEntity {

}