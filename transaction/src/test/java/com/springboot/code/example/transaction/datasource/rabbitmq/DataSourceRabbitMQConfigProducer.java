package com.springboot.code.example.transaction.datasource.rabbitmq;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.transaction.annotation.Transactional;
import com.springboot.code.example.transaction.domain.DogEntity;
import com.springboot.code.example.transaction.domain.DogRepo;
import com.springboot.code.example.utils.Strings;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
class DataSourceRabbitMQConfigProducer {

  private final Queue queue;
  private final RabbitTemplate rabbitTemplate;
  private final DogRepo dogRepo;

  @Transactional
  void send(boolean hasTransaction) {

    this.rabbitTemplate.setChannelTransacted(hasTransaction);

    var dog = new DogEntity(1, "Chiahaha");
    dogRepo.save(dog);
    this.rabbitTemplate.convertAndSend(queue.getName(), Strings.toSafeString(dog));

    throw new RuntimeException("Failure save dog");
  }

  void sendWithoutTransaction(boolean hasTransaction) {

    this.send(hasTransaction);
  }

}