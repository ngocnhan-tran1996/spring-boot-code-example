package com.springboot.code.example.rabbitmq.reply;

import static org.assertj.core.api.Assertions.assertThat;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import com.springboot.code.example.rabbitmq.BaseConfig;
import com.springboot.code.example.rabbitmq.EnableTestcontainers;
import com.springboot.code.example.testcase.TestCase;

@ActiveProfiles("replyAddress")
@SpringBootTest
@EnableTestcontainers
@Import({BaseConfig.class, ReplyListenerConfig.class})
class ReplyAddressTest {

  @Autowired
  RabbitTemplate rabbitTemplate;

  @Autowired
  Queue queue;

  @Autowired
  Queue replyQueue;

  @TestCase("com.springboot.code.example.rabbitmq.reply.testcase.ReplyTestArguments#testMessagePropsArguments")
  void testRabbitTemplateReply(
      String input,
      String expectedOutput)
      throws Exception {

    var actualOutput = (String) this.rabbitTemplate.convertSendAndReceive(
        queue.getName(),
        input);
    assertThat(actualOutput).isEqualTo(expectedOutput);
  }

}