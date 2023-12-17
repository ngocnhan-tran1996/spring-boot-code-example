package com.springboot.code.example.logger.testcase;

import java.util.List;
import org.apache.logging.log4j.Level;
import com.springboot.code.example.testcase.TestArguments;

class Log4j2TestArguments {

  static List<TestArguments<String[], String[]>> testLogArguments = List.of(
      TestArguments.ofSame(new String[] {Level.INFO.name(), "Hello, INFO!"}),
      TestArguments.ofSame(new String[] {Level.ALL.name(), "Hello, ALL!"}),
      TestArguments.ofSame(new String[] {Level.TRACE.name(), "Hello, TRACE!"}),
      TestArguments.ofSame(new String[] {Level.DEBUG.name(), "Hello, DEBUG!"}),
      TestArguments.ofSame(new String[] {Level.ERROR.name(), "Hello, ERROR!"}));

}
