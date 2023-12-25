package com.springboot.code.example.database.domain.name;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class NamePrefixResponse {

  private String firstName;
  private String lastName;
  private BigDecimal age;

}