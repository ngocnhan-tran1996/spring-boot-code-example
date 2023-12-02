package com.springboot.code.example.rabbitmq.listener;

import org.springframework.amqp.core.AnonymousQueue;
import org.springframework.amqp.core.Queue;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
class ListenerConfig {

  @Bean
  Queue anonymousQueue() {

    return new AnonymousQueue();
  }

  @Bean
  Queue rpcQueue() {

    return new AnonymousQueue();
  }

  @Bean
  Listener listener() {

    return new Listener();
  }

}