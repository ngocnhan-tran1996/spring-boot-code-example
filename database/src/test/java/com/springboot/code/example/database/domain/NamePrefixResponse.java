package com.springboot.code.example.database.domain;

import java.math.BigDecimal;

public record NamePrefixResponse(String firstName, String lastName, BigDecimal age) {

}