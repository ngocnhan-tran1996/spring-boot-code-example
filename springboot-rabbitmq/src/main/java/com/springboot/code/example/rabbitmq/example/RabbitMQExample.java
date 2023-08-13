package com.springboot.code.example.rabbitmq.example;

import java.util.Arrays;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.listener.RabbitListenerEndpointRegistry;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
@RequiredArgsConstructor
public class RabbitMQExample {

  private final RabbitListenerEndpointRegistry rabbitListenerEndpointRegistry;
  private final AmqpAdmin amqpAdmin;

  @RabbitListener(queuesToDeclare = @Queue(name = "local_CREATE_QUEUE"))
  public void listenNewQueue(String newQueue) {
    var container = (SimpleMessageListenerContainer) rabbitListenerEndpointRegistry
        .getListenerContainer("local");

    // avoid existed queue
    if (Arrays.binarySearch(container.getQueueNames(), newQueue) == -1) {
      log.info("Create queue {}", newQueue);
      amqpAdmin.declareQueue(new org.springframework.amqp.core.Queue(newQueue));
      container.addQueueNames(newQueue);
    }
  }

  @RabbitListener(id = "local")
  public void listenQueueById(String message) {

    log.info("Message: {}", message);
  }

}
