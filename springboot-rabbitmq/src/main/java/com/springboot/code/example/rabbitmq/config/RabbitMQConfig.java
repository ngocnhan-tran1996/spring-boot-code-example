package com.springboot.code.example.rabbitmq.config;

import java.util.stream.Stream;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Declarables;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

  @Value("${app.queue.name}")
  String queueName;

  @Value("${app.queue.names}")
  String[] queueNames;

  // basic create queue, it will be created if it is removed by client
  @Bean
  Queue queue() {
    return new Queue(queueName);
  }

  /**
   * create multiple queues
   * if one of themes is removed, it will show error notification
   */
  @Bean
  Declarables queues() {

    var queues = Stream.of(queueNames)
        .map(Queue::new)
        .toList();

    return new Declarables(queues);
  }

  /**
   * create multiple queues
   * if one of themes is removed, it will show error notification
   */
  @Bean
  RabbitMQCreator rabbitMQCreator(AmqpAdmin amqpAdmin) {
    var rabbitMQCreator = new RabbitMQCreator(amqpAdmin);
    rabbitMQCreator.execute(queueNames);
    return rabbitMQCreator;
  }

}