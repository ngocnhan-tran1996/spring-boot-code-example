package com.springboot.code.example.utils.testcase;

import java.util.List;
import com.springboot.code.example.testcase.TestArguments;

class PatternsTestArguments {

  private static String[] EMPTY_ARRAY = {};

  static List<TestArguments<String, String[]>> testGetClassNameAndVariableNameArguments;

  static {

    String[] expectOutput = {"com.example.PatternsTestCase", "Arguments"};
    testGetClassNameAndVariableNameArguments = List.of(
        TestArguments.of(null, EMPTY_ARRAY),
        TestArguments.of("", EMPTY_ARRAY),
        TestArguments.of("Hello", EMPTY_ARRAY),
        TestArguments.of("com.example.PatternsTestCase#Arguments", expectOutput));
  }

}
