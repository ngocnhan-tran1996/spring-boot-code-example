package com.springboot.code.example.database.problem.entity;

import java.math.BigDecimal;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "CAR")
@NoArgsConstructor
@AllArgsConstructor
public class CarEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private BigDecimal id;

  private String name;

}