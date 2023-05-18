package com.springboot.code.example.rabbitmq.consumer;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
public class MessageConsumer {

  @RabbitListener(queues = "#{'${app.queue.names}'.split(',')}")
  public void receive(Message message) {

    var messageProperties = message.getMessageProperties();
    log.info("Receive message {} from queue {} with exchange {}",
        new String(message.getBody()),
        messageProperties.getConsumerQueue(),
        messageProperties.getReceivedExchange());
  }

}