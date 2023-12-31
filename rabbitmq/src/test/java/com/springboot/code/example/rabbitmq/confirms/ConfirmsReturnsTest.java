package com.springboot.code.example.rabbitmq.confirms;

import static org.assertj.core.api.Assertions.assertThat;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueInformation;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import com.springboot.code.example.container.EnableTestcontainers;
import com.springboot.code.example.container.RabbitMQContainerInitializer;
import com.springboot.code.example.rabbitmq.BaseConfig;
import com.springboot.code.example.rabbitmq.confirms.testcase.ConfirmsReturnsTestArguments.ConfirmsReturnsArgumentsInput;
import com.springboot.code.example.rabbitmq.confirms.testcase.ConfirmsReturnsTestArguments.ConfirmsReturnsArgumentsOutput;
import com.springboot.code.example.testcase.TestCase;
import com.springboot.code.example.utils.Strings;

@ActiveProfiles("confirms")
@SpringBootTest(classes = {BaseConfig.class})
@EnableTestcontainers(RabbitMQContainerInitializer.class)
class ConfirmsReturnsTest {

  @Autowired
  RabbitAdmin rabbitAdmin;

  @Autowired
  RabbitTemplate rabbitTemplate;

  @Autowired
  Queue queue;

  @TestCase
  void testConfirmsReturns(
      ConfirmsReturnsArgumentsInput input,
      ConfirmsReturnsArgumentsOutput expectedOutput)
      throws Exception {

    String queueName = Strings.getIfNotBlank(input.queueName(), queue.getName());

    var correlationData = new CorrelationData(input.message());
    this.rabbitTemplate.convertAndSend(
        input.exchange(),
        queueName,
        input.message(),
        correlationData);
    var confirm = correlationData.getFuture().get(1, TimeUnit.SECONDS);
    assertThat(confirm)
        .usingRecursiveComparison()
        .isEqualTo(expectedOutput.confirm());

    var queueInfo = this.rabbitAdmin.getQueueInfo(queueName);
    Integer messageCount = Optional.ofNullable(queueInfo)
        .map(QueueInformation::getMessageCount)
        .orElse(-1); // -1 means message do not exist, avoid compare null integer value
    assertThat(messageCount)
        .isEqualTo(expectedOutput.messageCount());

    var msg = Optional.ofNullable(queueInfo)
        .map(QueueInformation::getName)
        .map(this.rabbitTemplate::receive)
        .map(Message::getBody)
        .map(String::new)
        .orElse(null);
    var expectedMsg = expectedOutput.messageCount() > 0
        ? input.message()
        : null;
    assertThat(msg)
        .isEqualTo(expectedMsg);
  }

}