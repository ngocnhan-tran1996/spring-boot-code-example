package com.springboot.code.example.log4j2.consumer;

import static org.assertj.core.api.Assertions.assertThat;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;
import lombok.extern.log4j.Log4j2;

@Log4j2
@SpringBootTest
@ExtendWith(OutputCaptureExtension.class)
class MessageConsumerTests {

  private static final List<String> OUTPUT = List.of(
      "\"password\" = \"test\"",
      "\"password\"=\"test\"",
      "\"password\"= \"test\"",
      "\"password\" =\"test\"",

      "\\\"password\\\" = \\\"test\\\"",
      "\"password\"=\\\"test\\\"",
      "\\\"password\\\"= \\\"test\\\"",
      "\\\"password\\\" =\\\"test\\\"",

      "\"password\" : \"test\"",
      "\"password\":\"test\"",
      "\"password\": \"test\"",
      "\"password\" :\"test\"",

      "\\\"password\\\" : \\\"test\\\"",
      "\"password\":\\\"test\\\"",
      "\\\"password\\\": \\\"test\\\"",
      "\\\"password\\\" :\\\"test\\\"");
  private static final List<String> MASK_OUTPUT = List.of(
      "\"password\" = \"****\"",
      "\"password\"=\"****\"",
      "\"password\"= \"****\"",
      "\"password\" =\"****\"",

      "\\\"password\\\" = \\\"****\\\"",
      "\"password\"=\\\"****\\\"",
      "\\\"password\\\"= \\\"****\\\"",
      "\\\"password\\\" =\\\"****\\\"",

      "\"password\" : \"****\"",
      "\"password\":\"****\"",
      "\"password\": \"****\"",
      "\"password\" :\"****\"",

      "\\\"password\\\" : \\\"****\\\"",
      "\"password\":\\\"****\\\"",
      "\\\"password\\\": \\\"****\\\"",
      "\\\"password\\\" :\\\"****\\\"");

  @Test
  void testMaskLog4j2(CapturedOutput capturedOutput) {

    OUTPUT.forEach(log::info);
    assertThat(capturedOutput.getOut()).contains(MASK_OUTPUT.toArray(String[]::new));
  }

}