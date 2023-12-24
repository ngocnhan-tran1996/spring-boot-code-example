package com.springboot.code.example.logger.testcase;

import org.apache.logging.log4j.Level;
import com.springboot.code.example.testcase.TestArguments;

class Log4j2TestArguments {

  static TestArguments testLogArguments = TestArguments
      .params(new String[] {Level.INFO.name(), "Hello, INFO!"})
      .nextParams(new String[] {Level.ALL.name(), "Hello, ALL!"})
      .nextParams(new String[] {Level.TRACE.name(), "Hello, TRACE!"})
      .nextParams(new String[] {Level.DEBUG.name(), "Hello, DEBUG!"})
      .nextParams(new String[] {Level.ERROR.name(), "Hello, ERROR!"});

}