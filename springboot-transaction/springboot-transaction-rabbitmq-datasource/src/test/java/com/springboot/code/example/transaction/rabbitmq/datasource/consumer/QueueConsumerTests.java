package com.springboot.code.example.transaction.rabbitmq.datasource.consumer;

import static org.assertj.core.api.Assertions.assertThat;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import com.springboot.code.example.transaction.rabbitmq.datasource.constant.QueueNames;
import com.springboot.code.example.transaction.rabbitmq.datasource.entity.HistoryEntity;
import com.springboot.code.example.transaction.rabbitmq.datasource.repository.HistoryRepository;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class QueueConsumerTests {

  @Autowired
  ConnectionFactory connectionFactory;

  @Autowired
  HistoryRepository historyRepository;

  @Autowired
  RabbitTemplate rabbitTemplate;

  CountDownLatch latch = new CountDownLatch(1);

  RabbitAdmin rabbitAdmin;

  @BeforeEach
  void init() {

    rabbitAdmin = new RabbitAdmin(rabbitTemplate);

    rabbitAdmin.declareQueue(new Queue(QueueNames.TRANSACTION_CONSUMER));
    rabbitAdmin.declareQueue(new Queue(QueueNames.TRANSACTION_SAME_CONSUMER));
    rabbitAdmin.declareQueue(new Queue(QueueNames.OUT_TRANSACTION_CONSUMER));
    rabbitAdmin.declareQueue(new Queue(QueueNames.TRANSACTION_RELATIVE_CONSUMER));
  }

  @Test
  @Order(1)
  void testListenWithoutTransaction() throws InterruptedException {

    rabbitAdmin.purgeQueue(QueueNames.OUT_TRANSACTION_PRODUCER);
    rabbitAdmin.purgeQueue(QueueNames.OUT_TRANSACTION_CONSUMER);
    latch.await(1, TimeUnit.SECONDS);

    rabbitTemplate.convertAndSend(QueueNames.OUT_TRANSACTION_PRODUCER, "without transaction");

    // receive message
    latch.await(1, TimeUnit.SECONDS);

    HistoryEntity historyEntity = (HistoryEntity) rabbitTemplate.receiveAndConvert(
        QueueNames.OUT_TRANSACTION_CONSUMER);
    assertThat(historyRepository.findById(historyEntity.getId())).isPresent()
        .get()
        .usingRecursiveComparison()
        .isEqualTo(historyEntity);
  }

  @Test
  @Order(2)
  void testListenInTransaction() throws InterruptedException {

    rabbitAdmin.purgeQueue(QueueNames.TRANSACTION_PRODUCER);
    rabbitAdmin.purgeQueue(QueueNames.TRANSACTION_CONSUMER);
    latch.await(1, TimeUnit.SECONDS);

    rabbitTemplate.convertAndSend(QueueNames.TRANSACTION_PRODUCER, "in transaction");

    // receive message
    latch.await(1, TimeUnit.SECONDS);

    HistoryEntity historyEntity = (HistoryEntity) rabbitTemplate.receiveAndConvert(
        QueueNames.TRANSACTION_CONSUMER);
    assertThat(historyRepository.findById(historyEntity.getId())).isEmpty();
  }

  @Test
  @Order(3)
  void testListenInSameTransaction() throws InterruptedException {

    rabbitAdmin.purgeQueue(QueueNames.TRANSACTION_SAME_PRODUCER);
    rabbitAdmin.purgeQueue(QueueNames.TRANSACTION_SAME_CONSUMER);
    latch.await(1, TimeUnit.SECONDS);

    rabbitTemplate.convertAndSend(QueueNames.TRANSACTION_SAME_PRODUCER, "in same transaction");

    // receive message
    latch.await(1, TimeUnit.SECONDS);

    HistoryEntity historyEntity = (HistoryEntity) rabbitTemplate.receiveAndConvert(
        QueueNames.TRANSACTION_SAME_CONSUMER);
    assertThat(historyEntity).isNull();
  }

  @Test
  @Order(4)
  void testListenInSameRelativeTransaction() throws InterruptedException {

    rabbitAdmin.purgeQueue(QueueNames.TRANSACTION_RELATIVE_PRODUCER);
    rabbitAdmin.purgeQueue(QueueNames.TRANSACTION_RELATIVE_CONSUMER);
    latch.await(1, TimeUnit.SECONDS);

    rabbitTemplate.convertAndSend(QueueNames.TRANSACTION_RELATIVE_PRODUCER, "in same transaction");

    // receive message
    latch.await(1, TimeUnit.SECONDS);

    HistoryEntity historyEntity = (HistoryEntity) rabbitTemplate.receiveAndConvert(
        QueueNames.TRANSACTION_RELATIVE_CONSUMER);
    assertThat(historyEntity).isNull();
  }

}
