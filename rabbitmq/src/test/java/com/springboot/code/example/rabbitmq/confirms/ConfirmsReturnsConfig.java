package com.springboot.code.example.rabbitmq.confirms;

import org.springframework.amqp.core.AnonymousQueue;
import org.springframework.amqp.core.Queue;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
class ConfirmsReturnsConfig {

  @Bean
  Queue confirmsQueue() {

    return new AnonymousQueue();
  }

}