package com.springboot.code.example.database.poly.datasource.oracle;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "cat")
public class CatEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;
  private String species;

  public CatEntity(String species) {

    this.species = species;
  }

}