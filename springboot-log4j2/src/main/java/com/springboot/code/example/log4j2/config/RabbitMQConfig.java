package com.springboot.code.example.log4j2.config;

import java.util.UUID;
import org.apache.logging.log4j.ThreadContext;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import com.springboot.code.example.common.helper.Strings;

@Aspect
@Component
public class RabbitMQConfig {

  @Pointcut("@annotation(org.springframework.amqp.rabbit.annotation.RabbitListener)")
  private void rabbitListenerAnnotation() {}

  @Before("rabbitListenerAnnotation() && args(id,..)")
  public void validateAccount(String id) {

    String threadId = Strings.getIfNotBlank(
        id,
        ThreadContext.get("ID"),
        UUID.randomUUID().toString());
    ThreadContext.put("ID", threadId);
  }

  @After("rabbitListenerAnnotation()")
  public void doReleaseThreadContext() {

    ThreadContext.clearMap();
  }

}
