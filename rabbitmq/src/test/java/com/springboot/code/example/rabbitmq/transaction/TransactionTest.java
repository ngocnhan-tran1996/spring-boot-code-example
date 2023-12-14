package com.springboot.code.example.rabbitmq.transaction;

import static org.assertj.core.api.Assertions.assertThat;
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
  void testTransaction() throws Exception {

    try {

      transactionProducer.send();
    } catch (Exception e) {

      // do nothing
    } finally {

      var msgCount = rabbitAdmin.getQueueInfo(queue.getName())
          .getMessageCount();
      assertThat(msgCount).isZero();
    }
  }

}