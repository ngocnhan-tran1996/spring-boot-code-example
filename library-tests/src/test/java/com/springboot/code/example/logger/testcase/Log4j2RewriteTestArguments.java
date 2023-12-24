package com.springboot.code.example.logger.testcase;

import java.util.List;
import com.springboot.code.example.testcase.TestArguments;

class Log4j2RewriteTestArguments {

  static List<TestArguments<String, String>> testMaskLog4j2Arguments = List.of(
      TestArguments.of("\"password\" = \"test\"", "\"password\" = \"****\""),
      TestArguments.of("\"password\"=\"test\"", "\"password\"=\"****\""),
      TestArguments.of("\"password\"= \"test\"", "\"password\"= \"****\""),
      TestArguments.of("\"password\" =\"test\"", "\"password\" =\"****\""),

      TestArguments.of("\\\"password\\\" = \\\"test\\\"", "\\\"password\\\" = \\\"****\\\""),
      TestArguments.of("\"password\"=\\\"test\\\"", "\"password\"=\\\"****\\\""),
      TestArguments.of("\\\"password\\\"= \\\"test\\\"", "\\\"password\\\"= \\\"****\\\""),
      TestArguments.of("\\\"password\\\" =\\\"test\\\"", "\\\"password\\\" =\\\"****\\\""),

      TestArguments.of("\"password\" : \"test\"", "\"password\" : \"****\""),
      TestArguments.of("\"password\":\"test\"", "\"password\":\"****\""),
      TestArguments.of("\"password\": \"test\"", "\"password\": \"****\""),
      TestArguments.of("\"password\" :\"test\"", "\"password\" :\"****\""),

      TestArguments.of("\\\"password\\\" : \\\"test\\\"", "\\\"password\\\" : \\\"****\\\""),
      TestArguments.of("\"password\":\\\"test\\\"", "\"password\":\\\"****\\\""),
      TestArguments.of("\\\"password\\\": \\\"test\\\"", "\\\"password\\\": \\\"****\\\""),
      TestArguments.of("\\\"password\\\" :\\\"test\\\"", "\\\"password\\\" :\\\"****\\\""));

}