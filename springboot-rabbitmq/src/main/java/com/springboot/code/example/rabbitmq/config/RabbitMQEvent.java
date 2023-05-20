package com.springboot.code.example.rabbitmq.config;

import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.listener.MissingQueueEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class RabbitMQEvent {

  private final AmqpAdmin amqpAdmin;

  @EventListener
  public void events(MissingQueueEvent event) {

    String queueName = event.getQueue();
    if (amqpAdmin.getQueueProperties(queueName) == null) {
      amqpAdmin.declareQueue(new Queue(queueName));
    }

  }

}