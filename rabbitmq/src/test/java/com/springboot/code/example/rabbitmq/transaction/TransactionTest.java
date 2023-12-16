package com.springboot.code.example.rabbitmq.transaction;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import java.util.concurrent.CountDownLatch;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import com.springboot.code.example.rabbitmq.BaseConfig;
import com.springboot.code.example.rabbitmq.EnableTestcontainers;

@SpringBootTest(classes = {BaseConfig.class, TransactionConfig.class})
@EnableTestcontainers
class TransactionTest {

  @Autowired
  RabbitAdmin rabbitAdmin;

  @Autowired
  RabbitTemplate rabbitTemplate;

  @Autowired
  Queue queue;

  @Autowired
  TransactionProducer transactionProducer;

  final CountDownLatch latch = new CountDownLatch(1);

  @Test
  void testSendWithTransaction() throws Exception {

    assertThatExceptionOfType(RuntimeException.class)
        .isThrownBy(transactionProducer::sendWithTransaction);
    var msgCount = rabbitAdmin.getQueueInfo(queue.getName())
        .getMessageCount();
    assertThat(msgCount).isZero();
  }

  @Test
  void testSendWithoutTransaction() throws Exception {

    assertThatExceptionOfType(RuntimeException.class)
        .isThrownBy(transactionProducer::sendWithoutTransaction);
    var msgCount = rabbitAdmin.getQueueInfo(queue.getName())
        .getMessageCount();
    assertThat(msgCount).isEqualTo(1);
  }

}