package com.springboot.code.example.rabbitmq.consumer;

import static org.assertj.core.api.Assertions.assertThat;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.test.RabbitListenerTest;
import org.springframework.amqp.rabbit.test.RabbitListenerTestHarness;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import com.springboot.code.example.rabbitmq.EnableTestcontainers;

@ActiveProfiles("byId")
@SpringBootTest(classes = ConsumerConfig.class)
@EnableTestcontainers
@RabbitListenerTest(spy = false, capture = true)
class ConsumerByIdTest {

  @Autowired
  RabbitAdmin rabbitAdmin;

  @Autowired
  RabbitTemplate rabbitTemplate;

  @Autowired
  RabbitListenerTestHarness harness;

  @Test
  void testListenNewQueue() throws Exception {

    String queueName = "local_test_queue";
    String msg = "Hello, world!";

    // validate queue has listened by 1 consumer at least
    rabbitTemplate.convertAndSend(ConsumerByIdListener.NEW_QUEUE_NAME, queueName);
    var queueInfo = rabbitAdmin.getQueueInfo(ConsumerByIdListener.NEW_QUEUE_NAME);
    assertThat(queueInfo.getConsumerCount()).isEqualTo(1);

    // send to new queue
    rabbitTemplate.convertAndSend(queueName, msg);

    var invocationData = harness.getNextInvocationDataFor(
        ConsumerByIdListener.LOCAL,
        1,
        TimeUnit.SECONDS);
    Object[] args = invocationData.getArguments();
    assertThat(args[0]).isEqualTo(msg);

    this.rabbitAdmin.deleteQueue(queueName);
    this.rabbitAdmin.deleteQueue(ConsumerByIdListener.NEW_QUEUE_NAME);
  }

}