package io.ngocnhan_tran1996.code.example.logger.testcase;

import io.ngocnhan_tran1996.code.example.testcase.TestArguments;
import org.apache.logging.log4j.Level;

class Log4j2TestArguments {

    static TestArguments testLogArguments = TestArguments
        .params(new String[]{Level.INFO.name(), "Hello, INFO!"})
        .nextParams(new String[]{Level.ALL.name(), "Hello, ALL!"})
        .nextParams(new String[]{Level.TRACE.name(), "Hello, TRACE!"})
        .nextParams(new String[]{Level.DEBUG.name(), "Hello, DEBUG!"})
        .nextParams(new String[]{Level.ERROR.name(), "Hello, ERROR!"});

}