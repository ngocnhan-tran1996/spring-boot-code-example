package com.springboot.code.example.rabbitmq.batch;

import java.util.ArrayList;
import java.util.List;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import lombok.extern.log4j.Log4j2;

@Log4j2
class AnnotationBatchListener {

  List<String> receiveMsg = new ArrayList<>();

  @RabbitListener(queues = "#{queue.name}", containerFactory = "consumerBatchContainerFactory")
  void receive(List<String> msg) {

    log.info("Hello, world");
    this.receiveMsg.addAll(msg);
  }

}