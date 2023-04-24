package com.springboot.code.example.rabbitmq.consumer;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
public class MessageConsumer {

  @RabbitListener(queues = {
      "local_QUEUE_TEST",
      "local_QUEUE_TEST_1",
      "local_QUEUE_TEST_2",
      "local_QUEUE_TEST_3",
      "local_QUEUE_TEST_4"
  })
  void receive(Message message) {

    log.info("{}", new String(message.getBody()));
  }

}
