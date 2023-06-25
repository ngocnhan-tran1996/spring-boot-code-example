package com.springboot.code.example.log4j2.consumer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Appender;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.Logger;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MessageConsumerTests {

  @Mock
  private Appender mockAppender;

  @Captor
  private ArgumentCaptor<LogEvent> captorLoggingEvent;

  private Logger logger;

  @BeforeEach
  public void setup() {
    // prepare the appender so Log4j likes it
    when(mockAppender.getName()).thenReturn("MockAppender");
    when(mockAppender.isStarted()).thenReturn(true);
    when(mockAppender.isStopped()).thenReturn(false);
 
    logger = (Logger)LogManager.getLogger(MessageConsumerTests.class);
    logger.addAppender(mockAppender);
    logger.setLevel(Level.INFO);
  }

  @AfterEach
  public void tearDown() {
    // the appender we added will sit in the singleton logger forever
    // slowing future things down - so remove it
    logger.removeAppender(mockAppender);
  }

  @Test
  public void loggingIsCaptured() {
    logger.error("What an error");
    verifyErrorMessages("What an error");
  }

  @Test
  public void noBleedBetweenLogCaptures() {
    logger.error("What an oopsie");
    verifyErrorMessages("What an oopsie");
  }

  @Test
  public void nothingLogged() {
    verifyErrorMessages();
  }

  // handy function to inspect the messages sent to the logger
  private void verifyErrorMessages(String... messages) {
    verify(mockAppender, times(messages.length))
        .append(captorLoggingEvent.capture());

    int i = 0;
    for (LogEvent loggingEvent : captorLoggingEvent.getAllValues()) {
      assertEquals(messages[i++],
          loggingEvent.getMessage().getFormattedMessage());
    }
  }

}