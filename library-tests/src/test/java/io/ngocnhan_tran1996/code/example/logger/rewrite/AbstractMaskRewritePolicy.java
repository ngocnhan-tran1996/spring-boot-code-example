package io.ngocnhan_tran1996.code.example.logger.rewrite;

import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.appender.rewrite.RewritePolicy;
import org.apache.logging.log4j.core.impl.Log4jLogEvent;
import org.apache.logging.log4j.message.SimpleMessage;

abstract class AbstractMaskRewritePolicy implements RewritePolicy {

  @Override
  public LogEvent rewrite(final LogEvent event) {

    String formattedMessage = event.getMessage()
        .getFormattedMessage();
    var messageBuilder = new StringBuilder(formattedMessage);
    this.mask(messageBuilder);

    var simpleMessage = new SimpleMessage(messageBuilder.toString());
    return new Log4jLogEvent.Builder(event)
        .setMessage(simpleMessage)
        .build();
  }

  protected abstract void mask(StringBuilder stringBuilder);

  protected abstract char getMaskCharacter();

}