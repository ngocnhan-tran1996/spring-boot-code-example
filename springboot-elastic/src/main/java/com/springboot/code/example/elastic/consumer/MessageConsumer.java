package com.springboot.code.example.elastic.consumer;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Component
public class MessageConsumer {

  @RabbitListener(queuesToDeclare = @Queue(name = "${app.queue.name}"))
  public void receive(Message message) {

    var messageProperties = message.getMessageProperties();
    log.info("Receive message {} from queue {} with exchange {}",
        new String(message.getBody()),
        messageProperties.getConsumerQueue(),
        messageProperties.getReceivedExchange());
    throw new IllegalArgumentException("Retry");
  }

  public void receiveListener(Message message) {

    this.receive(message);
  }

}
