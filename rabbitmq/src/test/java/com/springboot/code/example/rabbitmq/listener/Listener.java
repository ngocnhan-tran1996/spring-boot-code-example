package com.springboot.code.example.rabbitmq.listener;

import java.util.concurrent.CountDownLatch;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;

class Listener {

  String receiveMsg = "";
  final CountDownLatch latch = new CountDownLatch(1);

  @RabbitListener(queues = "#{queue.name}")
  void receive(@Payload String msg, @Header(AmqpHeaders.CONSUMER_QUEUE) String queueName) {

    this.receiveMsg = msg;
  }

}