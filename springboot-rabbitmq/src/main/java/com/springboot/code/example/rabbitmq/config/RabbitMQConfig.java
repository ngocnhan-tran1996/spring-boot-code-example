package com.springboot.code.example.rabbitmq.config;

import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

  @Bean
  AmqpAdmin amqpAdmin(ConnectionFactory connectionFactory) {

    return new RabbitAdmin(connectionFactory);
  }

}