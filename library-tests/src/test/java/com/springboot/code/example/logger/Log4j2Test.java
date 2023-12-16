package com.springboot.code.example.logger;

import static org.assertj.core.api.Assertions.assertThat;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.appender.ConsoleAppender;
import com.springboot.code.example.testcase.TestCase;
import lombok.extern.log4j.Log4j2;

@Log4j2
class Log4j2Test {

  @TestCase
  void testLog(String[] input, String[] output) {

    final var appenders = ((org.apache.logging.log4j.core.Logger) log).getAppenders();
    final var consoleAppender = (ConsoleAppender) appenders.get("Console");

    /**
     * 0 for level
     * 1 for message
     */
    log.log(Level.getLevel(input[0]), input[1]);
    var byteBuffer = consoleAppender.getManager()
        .getByteBuffer();
    String actualOutput = new String(byteBuffer.array());

    assertThat(actualOutput).contains(output);
  }

}
