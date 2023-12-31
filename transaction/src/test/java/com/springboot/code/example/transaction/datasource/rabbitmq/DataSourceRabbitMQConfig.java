package com.springboot.code.example.transaction.datasource.rabbitmq;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import com.springboot.code.example.transaction.domain.DogRepo;

@TestConfiguration
class DataSourceRabbitMQConfig {

  @Bean
  DataSourceRabbitMQConfigProducer producer(
      Queue queue,
      RabbitTemplate rabbitTemplate,
      DogRepo dogRepo) {

    return new DataSourceRabbitMQConfigProducer(queue, rabbitTemplate, dogRepo);
  }

}