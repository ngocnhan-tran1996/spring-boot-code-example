package com.springboot.code.example.utils.testcase;

import java.util.List;
import com.springboot.code.example.testcase.TestArguments;

class StringsTestArguments {

  static List<TestArguments<String, Boolean>> testIsBlankArguments = List.of(
      TestArguments.of(null, true),
      TestArguments.of("", true),
      TestArguments.of(" ", true),
      TestArguments.of("null", false),
      TestArguments.of("not blank", false));

  static List<TestArguments<String[], String>> testJoinArguments = List.of(
      TestArguments.of(null, null),
      TestArguments.of(new String[] {"Hello", "World"}, "HelloWorld"),
      TestArguments.of(new String[] {null, null}, "nullnull"),
      TestArguments.of(new String[] {"Hello", ",", " ", "World", "!"}, "Hello, World!"));

}