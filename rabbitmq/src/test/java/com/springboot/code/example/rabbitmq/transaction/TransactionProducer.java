package com.springboot.code.example.rabbitmq.transaction;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;

@Transactional
@RequiredArgsConstructor
class TransactionProducer {

  private final Queue queue;
  private final RabbitTemplate rabbitTemplate;

  void send() {

    this.rabbitTemplate.convertAndSend(queue.getName(), "test");
    throw new RuntimeException();
  }

}