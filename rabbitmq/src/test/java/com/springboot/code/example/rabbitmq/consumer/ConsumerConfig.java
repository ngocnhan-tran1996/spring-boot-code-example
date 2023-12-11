package com.springboot.code.example.rabbitmq.consumer;

import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
class ConsumerConfig {

  @Bean
  ConsumerListener consumerListener() {

    return new ConsumerListener();
  }

  @Bean
  ConsumerEvent consumerEvent(AmqpAdmin amqpAdmin) {

    return new ConsumerEvent(amqpAdmin);
  }

}