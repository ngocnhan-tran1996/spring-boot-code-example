package com.springboot.code.example.rabbitmq.config;

import java.util.stream.Stream;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Queue;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class RabbitMQCreator {

  private final AmqpAdmin amqpAdmin;

  public void execute(String[] queueNames) {

    Stream.of(queueNames)
        .forEach(queue -> amqpAdmin.declareQueue(new Queue(queue)));
  }

}