package com.springboot.code.example.utils.testcase;

import com.springboot.code.example.testcase.TestArguments;

class PatternsTestArguments {

  private static final String[] EMPTY_ARRAY = {};
  private static final String[] EXPECT_OUTPUT = {"com.example.PatternsTestCase", "Arguments"};

  static TestArguments testGetClassNameAndVariableNameArguments = TestArguments
      .params(null, EMPTY_ARRAY)
      .nextParams("", EMPTY_ARRAY)
      .nextParams("Hello", EMPTY_ARRAY)
      .nextParams("com.example.PatternsTestCase#Arguments", EXPECT_OUTPUT);

}