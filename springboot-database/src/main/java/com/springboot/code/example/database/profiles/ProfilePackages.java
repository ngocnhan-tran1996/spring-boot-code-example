package com.springboot.code.example.database.profiles;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ProfilePackages {

  private static final String PREFIX_PACKAGE = "com.springboot.code.example.database.";

  public static final String MULTIPLE_DATASOURCE = PREFIX_PACKAGE + Profiles.MULTIPLE_DATASOURCE;
  public static final String ENTITY_MANAGER = PREFIX_PACKAGE + Profiles.ENTITY_MANAGER;
  public static final String JDBC = PREFIX_PACKAGE + Profiles.JDBC;

}
