package com.springboot.code.example.transaction.rabbitmq;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import com.springboot.code.example.testcase.TestCase;
import com.springboot.code.example.transaction.container.EnableTestcontainers;

@EnableAutoConfiguration(exclude = DataSourceAutoConfiguration.class)
@SpringBootTest(classes = {BaseConfig.class, TransactionConfig.class})
@EnableTestcontainers
class TransactionTest {

  @Autowired
  RabbitAdmin rabbitAdmin;

  @Autowired
  Queue queue;

  @Autowired
  TransactionProducer transactionProducer;

  @TestCase
  void testSend(boolean input, int output) throws Exception {

    assertThatExceptionOfType(RuntimeException.class)
        .isThrownBy(() -> transactionProducer.send(input));
    var msgCount = rabbitAdmin.getQueueInfo(queue.getName())
        .getMessageCount();
    assertThat(msgCount).isEqualTo(output);
  }

}