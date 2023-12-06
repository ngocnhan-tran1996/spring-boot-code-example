package com.springboot.code.example.rabbitmq.batch;

import org.springframework.amqp.core.AnonymousQueue;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.batch.SimpleBatchingStrategy;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.BatchingRabbitTemplate;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

@TestConfiguration
class BatchConfig {

  @Bean
  Queue batchQueue() {

    return new AnonymousQueue();
  }

  @Profile("batch")
  @Bean
  BatchingRabbitTemplate batchingRabbitTemplate(ConnectionFactory connectionFactory) {

    var scheduler = new ThreadPoolTaskScheduler();
    scheduler.initialize();

    var batchingStrategy = new SimpleBatchingStrategy(
        10,
        Integer.MAX_VALUE,
        500);

    return new BatchingRabbitTemplate(connectionFactory, batchingStrategy, scheduler);
  }

  @Profile("annotation-batch")
  @Bean
  AnnotationBatch annotationBatch() {

    return new AnnotationBatch();
  }

}