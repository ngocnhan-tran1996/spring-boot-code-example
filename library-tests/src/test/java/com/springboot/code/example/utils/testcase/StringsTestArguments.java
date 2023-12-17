package com.springboot.code.example.utils.testcase;

import java.math.BigDecimal;
import java.util.List;
import com.springboot.code.example.testcase.TestArguments;

class StringsTestArguments {

  static List<TestArguments<String, Boolean>> testIsBlankArguments = List.of(
      TestArguments.of(null, true),
      TestArguments.of("", true),
      TestArguments.of(" ", true),
      TestArguments.of("null", false),
      TestArguments.of("not blank", false));

  static List<TestArguments<Object[], String>> testJoinArguments = List.of(
      TestArguments.of(null, null),
      TestArguments.of(new String[] {"Hello", "World"}, "HelloWorld"),
      TestArguments.of(new String[] {null, null}, "nullnull"),
      TestArguments.of(new String[] {"Hello", ",", " ", "World", "!"}, "Hello, World!"),
      TestArguments.of(new Object[] {"Hello", 1, BigDecimal.ONE, 1.5}, "Hello111.5"));

  static List<TestArguments<? extends Object, String>> testToSafeStringArguments = List.of(
      TestArguments.of(null, "null"),
      TestArguments.ofSame("HelloWorld"),
      TestArguments.of(1, "1"),
      TestArguments.of(new StringsTestArguments(), "null"));

  static List<TestArguments<String[], String>> testGetIfNotBlankArguments = List.of(
      TestArguments.of((String[]) null, null), TestArguments.of((String[]) null, null),
      TestArguments.of(new String[0], null),
      TestArguments.of(new String[] {null, "", " "}, null),
      TestArguments.of(new String[] {null, null, " "}, null),
      TestArguments.of(new String[] {null, "zz"}, "zz"),
      TestArguments.of(new String[] {"abc"}, "abc"),
      TestArguments.of(new String[] {null, "xyz"}, "xyz"),
      TestArguments.of(new String[] {null, "xyz", "abc"}, "xyz"));
}