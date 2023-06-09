package com.springboot.code.example.database.jdbc.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class JdbcComplexTypes {

  public static final String PACK_EXAMPLE = "example_pack";

  // type package
  public static final String PERSON_ARRAY = PACK_EXAMPLE + ".PERSON_ARRAY";
  public static final String PERSON_RECORD = PACK_EXAMPLE + ".PERSON_RECORD";

  // parameter
  public static final String RESULT = "result";
  public static final String OUTPUT_NUMBER = "OUT_NBR";

}