package com.springboot.code.example.transaction.jpa.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class QueryStrings {

  public static final String INSERT_STATEMENT = "INSERT INTO QUEUE_HISTORY VALUES (default, :name)";

}