package com.springboot.code.example.rabbitmq.create.queue;

import org.springframework.amqp.core.Declarables;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

  // basic create queue
  @Bean
  Queue queue() {
    return new Queue("local_QUEUE_TEST");
  }

  /**
   * create multiple queues
   * if one of themes is removed, it will show error notification
   */
  @Bean
  Declarables queues() {
    return new Declarables(
        new Queue("local_QUEUE_TEST_1"),
        new Queue("local_QUEUE_TEST_2"));
  }

  /**
   * create multiple queues
   * if one of themes is removed, it will create removed queue
   */
  @Bean
  RabbitAdmin rabbitAdmin(ConnectionFactory connectionFactory) {
    return new RabbitAdmin(connectionFactory);
  }

  @Bean
  RabbitMQCreator rabbitMQCreator(RabbitAdmin rabbitAdmin, RabbitTemplate rabbitTemplate) {
    var rabbitMQCreator = new RabbitMQCreator(rabbitAdmin, rabbitTemplate);
    rabbitMQCreator.execute();
    return rabbitMQCreator;
  }

}