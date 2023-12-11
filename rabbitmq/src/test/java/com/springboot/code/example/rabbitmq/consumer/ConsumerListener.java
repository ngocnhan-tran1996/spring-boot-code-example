package com.springboot.code.example.rabbitmq.consumer;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import lombok.extern.log4j.Log4j2;

@Log4j2
class ConsumerListener {

  static final String QUEUE_NAME = "consumer.queue.test";

  @RabbitListener(queues = QUEUE_NAME)
  void receive(String msg) {

    log.debug("Receive {}", msg);
  }

}