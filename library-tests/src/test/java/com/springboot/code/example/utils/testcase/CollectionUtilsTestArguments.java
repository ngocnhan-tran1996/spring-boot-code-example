package com.springboot.code.example.utils.testcase;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.springboot.code.example.testcase.TestArguments;

class CollectionUtilsTestArguments {

  static List<TestArguments<String[], Boolean>> testIsEmptyStringArrayArguments = List.of(
      TestArguments.of(null, true),
      TestArguments.of(new String[0], true),
      TestArguments.of(new String[] {"Hello", "World"}, false),
      TestArguments.of(new String[] {null, null}, false));

  static List<TestArguments<Map<?, ?>, Boolean>> testIsEmptyMapArguments = List.of(
      TestArguments.of(null, true),
      TestArguments.of(Map.of(), true),
      TestArguments.of(new HashMap<>(), true),
      TestArguments.of(Map.of("1", "1"), false));

}
