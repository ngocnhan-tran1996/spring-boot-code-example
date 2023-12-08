package com.springboot.code.example.rabbitmq.reply;

import static org.assertj.core.api.Assertions.assertThat;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import com.springboot.code.example.rabbitmq.BaseConfig;
import com.springboot.code.example.rabbitmq.EnableTestcontainers;
import com.springboot.code.example.testcase.TestCase;

@SpringBootTest
@EnableTestcontainers
@Import({BaseConfig.class, ReplyListenerConfig.class})
class ReplyTest {

  @Autowired
  RabbitTemplate rabbitTemplate;

  @Autowired
  Queue queue;

  @Autowired
  Queue replyQueue;

  final CountDownLatch latch = new CountDownLatch(1);

  @TestCase
  void testMessageProps(
      String input,
      String expectedOutput)
      throws Exception {

    this.rabbitTemplate.convertAndSend(queue.getName(), input, message -> {

      message.getMessageProperties().setReplyTo(replyQueue.getName());
      message.getMessageProperties().setCorrelationId(UUID.randomUUID().toString());
      return message;
    });
    latch.await(1, TimeUnit.SECONDS);

    String actualOutput = (String) this.rabbitTemplate.receiveAndConvert(replyQueue.getName());
    assertThat(actualOutput).isEqualTo(expectedOutput);
  }

}