package com.springboot.code.example.database.domain.name;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "NAME_PREFIX")
public class NamePrefixEntity {

  @Id
  @Column(name = "id")
  private BigDecimal id;

  @Column(name = "FIRST_NAME")
  private String firstName;

  @Column(name = "LAST_NAME")
  private String lastName;

  @Column(name = "AGE")
  private BigDecimal age;

  @Column(name = "CREATEDATETIME")
  private LocalDateTime createDateTime;

}