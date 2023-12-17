package com.springboot.code.example.rabbitmq.batch;

import org.springframework.amqp.rabbit.batch.SimpleBatchingStrategy;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.BatchingRabbitTemplate;
import org.springframework.boot.autoconfigure.amqp.SimpleRabbitListenerContainerFactoryConfigurer;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

@TestConfiguration
class BatchConfig {

  @Profile("batch")
  @Bean
  BatchingRabbitTemplate batchingRabbitTemplate(ConnectionFactory connectionFactory) {

    var scheduler = new ThreadPoolTaskScheduler();
    scheduler.initialize();

    var batchingStrategy = new SimpleBatchingStrategy(
        10,
        Integer.MAX_VALUE,
        100);

    return new BatchingRabbitTemplate(connectionFactory, batchingStrategy, scheduler);
  }

  @Profile("annotation-batch")
  @Bean
  AnnotationBatchListener annotationBatchListener() {

    return new AnnotationBatchListener();
  }

  @Profile("annotation-batch")
  @Bean("consumerBatchContainerFactory")
  SimpleRabbitListenerContainerFactory consumerBatchContainerFactory(
      SimpleRabbitListenerContainerFactoryConfigurer configurer,
      ConnectionFactory connectionFactory) {

    var factory = new SimpleRabbitListenerContainerFactory();
    configurer.configure(factory, connectionFactory);
    factory.setReceiveTimeout(100L);
    return factory;
  }
}