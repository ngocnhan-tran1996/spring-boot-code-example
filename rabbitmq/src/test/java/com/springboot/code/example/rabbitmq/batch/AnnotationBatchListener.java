package com.springboot.code.example.rabbitmq.batch;

import java.util.ArrayList;
import java.util.List;
import org.springframework.amqp.rabbit.annotation.RabbitListener;

class AnnotationBatchListener {

  List<String> receiveMsg = new ArrayList<>();

  @RabbitListener(queues = "#{queue.name}", containerFactory = "consumerBatchContainerFactory")
  void receive(List<String> msg) {

    this.receiveMsg.addAll(msg);
  }

}