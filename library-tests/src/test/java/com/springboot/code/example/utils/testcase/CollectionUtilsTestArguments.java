package com.springboot.code.example.utils.testcase;

import java.util.List;
import com.springboot.code.example.testcase.TestArguments;

class CollectionUtilsTestArguments {

  static List<TestArguments<String[], Boolean>> testIsEmptyArguments = List.of(
      TestArguments.of(null, true),
      TestArguments.of(new String[0], true),
      TestArguments.of(new String[] {"Hello", "World"}, false),
      TestArguments.of(new String[] {null, null}, false));

}
