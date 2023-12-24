package com.springboot.code.example.logger.testcase;

import com.springboot.code.example.testcase.TestArguments;

class Log4j2RewriteTestArguments {

  static TestArguments testMaskLog4j2Arguments = TestArguments
      .params("\"password\" = \"test\"", "\"password\" = \"****\"")
      .nextParams("\"password\"=\"test\"", "\"password\"=\"****\"")
      .nextParams("\"password\"= \"test\"", "\"password\"= \"****\"")
      .nextParams("\"password\" =\"test\"", "\"password\" =\"****\"")

      .nextParams("\\\"password\\\" = \\\"test\\\"", "\\\"password\\\" = \\\"****\\\"")
      .nextParams("\"password\"=\\\"test\\\"", "\"password\"=\\\"****\\\"")
      .nextParams("\\\"password\\\"= \\\"test\\\"", "\\\"password\\\"= \\\"****\\\"")
      .nextParams("\\\"password\\\" =\\\"test\\\"", "\\\"password\\\" =\\\"****\\\"")

      .nextParams("\"password\" : \"test\"", "\"password\" : \"****\"")
      .nextParams("\"password\":\"test\"", "\"password\":\"****\"")
      .nextParams("\"password\": \"test\"", "\"password\": \"****\"")
      .nextParams("\"password\" :\"test\"", "\"password\" :\"****\"")

      .nextParams("\\\"password\\\" : \\\"test\\\"", "\\\"password\\\" : \\\"****\\\"")
      .nextParams("\"password\":\\\"test\\\"", "\"password\":\\\"****\\\"")
      .nextParams("\\\"password\\\": \\\"test\\\"", "\\\"password\\\": \\\"****\\\"")
      .nextParams("\\\"password\\\" :\\\"test\\\"", "\\\"password\\\" :\\\"****\\\"");

}