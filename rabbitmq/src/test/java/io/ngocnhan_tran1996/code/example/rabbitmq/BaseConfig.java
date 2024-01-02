package io.ngocnhan_tran1996.code.example.rabbitmq;

import org.springframework.amqp.core.AnonymousQueue;
import org.springframework.amqp.core.Queue;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class BaseConfig {

  @Bean
  Queue queue() {

    return new AnonymousQueue();
  }

}
