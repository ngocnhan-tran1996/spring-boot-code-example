package com.springboot.code.example.database.profiles;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class DatabaseProfiles {

  public static final String MULTIPLE_DATASOURCE = "multiple.datasource";
  public static final String ENTITY_MANAGER = "entitymanager";
  public static final String JDBC = "jdbc";

}
