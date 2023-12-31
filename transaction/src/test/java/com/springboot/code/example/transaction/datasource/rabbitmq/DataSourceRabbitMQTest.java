package com.springboot.code.example.transaction.datasource.rabbitmq;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import com.springboot.code.example.container.BrokerContainer;
import com.springboot.code.example.container.DatabaseContainer;
import com.springboot.code.example.testcase.TestCase;
import com.springboot.code.example.transaction.rabbitmq.BaseConfig;

@ActiveProfiles("postgres")
@SpringBootTest(classes = {
    BaseConfig.class,
    BrokerContainer.class,
    DatabaseContainer.class,
    DataSourceRabbitMQConfig.class
})
class DataSourceRabbitMQTest {

  @Autowired
  RabbitAdmin rabbitAdmin;

  @Autowired
  Queue queue;

  @Autowired
  DataSourceRabbitMQConfigProducer producer;

  @BeforeEach
  void init() {

    this.rabbitAdmin.purgeQueue(this.queue.getName());
  }

  @TestCase("com.springboot.code.example.transaction.rabbitmq.testcase.TransactionTestArguments#testSendArguments")
  void testSend(boolean input, int output) throws Exception {

    assertThatExceptionOfType(RuntimeException.class)
        .isThrownBy(() -> producer.send(input));
    var msgCount = rabbitAdmin.getQueueInfo(queue.getName())
        .getMessageCount();
    assertThat(msgCount).isEqualTo(output);
  }

  @TestCase("com.springboot.code.example.transaction.rabbitmq.testcase.TransactionTestArguments#testSendWithCloneArguments")
  void testSendWithoutTransaction(boolean input, int output) throws Exception {

    assertThatExceptionOfType(RuntimeException.class)
        .isThrownBy(() -> producer.sendWithoutTransaction(input));
    var msgCount = rabbitAdmin.getQueueInfo(queue.getName())
        .getMessageCount();
    assertThat(msgCount).isEqualTo(output);
  }

}