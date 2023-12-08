package com.springboot.code.example.rabbitmq.rpc;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
class ListenerConfig {

  @Bean
  Listener listener() {

    return new Listener();
  }

}