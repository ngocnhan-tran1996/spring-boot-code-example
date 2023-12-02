package com.springboot.code.example.rabbitmq.listener;

import java.util.concurrent.CountDownLatch;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;

class Listener {

  String receiveMsg = "";
  CountDownLatch latch = new CountDownLatch(1);

  @RabbitListener(queues = "#{anonymousQueue.name}")
  void receive(@Payload String msg, @Header(AmqpHeaders.CONSUMER_QUEUE) String queueName) {

    this.receiveMsg = msg;
  }

  @RabbitListener(queues = "#{rpcQueue.name}")
  String rpc(String msg) {

    return msg + "_RPC";
  }

}
