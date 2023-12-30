package com.springboot.code.example.transaction.rabbitmq;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import java.util.concurrent.CountDownLatch;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import com.springboot.code.example.testcase.TestCase;

@EnableAutoConfiguration(exclude = DataSourceAutoConfiguration.class)
@SpringBootTest(classes = TransactionConfig.class)
@EnableTestcontainers
class TransactionTest {

  @Autowired
  RabbitAdmin rabbitAdmin;

  @Autowired
  Queue queue;

  @Autowired
  TransactionProducer transactionProducer;

  final CountDownLatch latch = new CountDownLatch(1);

  @TestCase
  void testSend(boolean input, int output) throws Exception {

    assertThatExceptionOfType(RuntimeException.class)
        .isThrownBy(() -> transactionProducer.send(input));
    var msgCount = rabbitAdmin.getQueueInfo(queue.getName())
        .getMessageCount();
    assertThat(msgCount).isEqualTo(output);
  }

}