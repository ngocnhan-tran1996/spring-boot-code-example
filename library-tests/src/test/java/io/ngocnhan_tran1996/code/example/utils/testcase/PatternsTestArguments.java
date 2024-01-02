package io.ngocnhan_tran1996.code.example.utils.testcase;

import io.ngocnhan_tran1996.code.example.testcase.TestArguments;

class PatternsTestArguments {

  private static final String[] EMPTY_ARRAY = {};

  static TestArguments testGetClassNameAndVariableNameArguments = TestArguments
      .params(null, EMPTY_ARRAY)
      .nextParams("", EMPTY_ARRAY)
      .nextParams("Hello", EMPTY_ARRAY)
      .nextParams("com.example.PatternsTestCase#Arguments",
          new String[] {"com.example.PatternsTestCase", "Arguments"})
      .nextParams("io.ngocnhan_tran1996.code.PatternsTestCase#Arguments",
          new String[] {"io.ngocnhan_tran1996.code.PatternsTestCase", "Arguments"});

}