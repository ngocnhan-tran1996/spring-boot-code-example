package io.ngocnhan_tran1996.code.example.logger.testcase;

import io.ngocnhan_tran1996.code.example.testcase.TestArguments;

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