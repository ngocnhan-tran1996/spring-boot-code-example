package com.springboot.code.example.database.domain.name;

import java.math.BigDecimal;

public interface NamePrefixProjection {

  String getFirstName();

  String getLastName();

  BigDecimal getAge();

}