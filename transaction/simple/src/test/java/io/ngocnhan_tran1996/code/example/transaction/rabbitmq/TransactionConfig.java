package io.ngocnhan_tran1996.code.example.transaction.rabbitmq;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.transaction.RabbitTransactionManager;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.TransactionManager;

@TestConfiguration
class TransactionConfig {

  @Bean
  TransactionManager transactionManager(ConnectionFactory connectionFactory) {

    return new RabbitTransactionManager(connectionFactory);
  }

  @Bean
  TransactionProducer transactionProducer(Queue queue, RabbitTemplate rabbitTemplate) {

    return new TransactionProducer(queue, rabbitTemplate);
  }

}