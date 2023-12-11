package com.springboot.code.example.rabbitmq.reply;

import static org.assertj.core.api.Assertions.assertThat;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import com.springboot.code.example.rabbitmq.BaseConfig;
import com.springboot.code.example.rabbitmq.EnableTestcontainers;
import com.springboot.code.example.testcase.TestCase;

@ActiveProfiles("send-to")
@SpringBootTest(classes = {BaseConfig.class, ReplyListenerConfig.class})
@EnableTestcontainers
class ReplySendToTest {

  @Autowired
  RabbitTemplate rabbitTemplate;

  @Autowired
  Queue queue;

  @Autowired
  Queue replyQueue;

  final CountDownLatch latch = new CountDownLatch(1);

  @TestCase("com.springboot.code.example.rabbitmq.reply.testcase.ReplyTestArguments#testMessagePropsArguments")
  void testSendTo(
      String input,
      String expectedOutput)
      throws Exception {

    this.rabbitTemplate.convertAndSend(queue.getName(), input);
    latch.await(1, TimeUnit.SECONDS);

    String actualOutput = (String) this.rabbitTemplate.receiveAndConvert(replyQueue.getName());
    assertThat(actualOutput).isEqualTo(expectedOutput);
  }

}