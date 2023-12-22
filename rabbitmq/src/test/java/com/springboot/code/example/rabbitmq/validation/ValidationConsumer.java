package com.springboot.code.example.rabbitmq.validation;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import jakarta.validation.Valid;
import lombok.extern.log4j.Log4j2;

@Log4j2
class ValidationConsumer {

  @RabbitListener(queues = "#{queue.name}", messageConverter = "jsonConverter")
  void consume(@Payload @Valid Employee employee) {

    log.info("Name : {}", employee);
  }

}