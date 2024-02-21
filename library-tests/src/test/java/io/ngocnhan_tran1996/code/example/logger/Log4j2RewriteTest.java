package io.ngocnhan_tran1996.code.example.logger;

import static org.assertj.core.api.Assertions.assertThat;

import io.ngocnhan_tran1996.code.example.testcase.TestCase;
import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.core.appender.ConsoleAppender;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@Log4j2
@ActiveProfiles("log4j2-mask")
@SpringBootTest
class Log4j2RewriteTest {

    @TestCase
    void testMaskLog4j2(String input, String output) {

        log.info(input);
        final var appenders = ((org.apache.logging.log4j.core.Logger) log).getContext()
            .getConfiguration()
            .getAppenders();
        final var consoleAppender = (ConsoleAppender) appenders.get("Console");
        final var byteBuffer = consoleAppender.getManager()
            .getByteBuffer();
        String actualOutput = new String(byteBuffer.array());
        assertThat(actualOutput).contains(output);
    }

}