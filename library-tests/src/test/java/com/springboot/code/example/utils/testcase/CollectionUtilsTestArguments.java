package com.springboot.code.example.utils.testcase;

import java.util.HashMap;
import java.util.Map;
import com.springboot.code.example.testcase.TestArguments;

class CollectionUtilsTestArguments {

  static TestArguments testIsEmptyObjectArrayArguments = TestArguments
      .params(null, true)
      .nextParams(new String[0], true)
      .nextParams(new String[] {"Hello", "World"}, false)
      .nextParams(new String[] {null, null}, false)
      .nextParams(new Object[0], true)
      .nextParams(new Object[] {null, null}, false)
      .nextParams(new Object[] {1, "true", null}, false);

  static TestArguments testIsEmptyMapArguments = TestArguments
      .params(null, true)
      .nextParams(Map.of(), true)
      .nextParams(new HashMap<>(), true)
      .nextParams(Map.of("1", "1"), false);

}
