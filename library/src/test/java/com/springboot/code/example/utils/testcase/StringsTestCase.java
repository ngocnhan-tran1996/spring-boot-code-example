package com.springboot.code.example.utils.testcase;

import java.util.List;
import org.junit.jupiter.params.provider.Arguments;

class StringsTestCase {

  static List<Arguments> testIsNotBlankArguments = List.of(
      Arguments.of(null, true), // null strings should be considered blank
      Arguments.of("", true),
      Arguments.of(" ", true),
      Arguments.of("not blank", false));

}
