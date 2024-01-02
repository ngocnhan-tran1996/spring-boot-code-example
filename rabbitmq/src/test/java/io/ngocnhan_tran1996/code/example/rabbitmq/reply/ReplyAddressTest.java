package io.ngocnhan_tran1996.code.example.rabbitmq.reply;

import static org.assertj.core.api.Assertions.assertThat;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import io.ngocnhan_tran1996.code.example.container.EnableTestcontainers;
import io.ngocnhan_tran1996.code.example.container.RabbitMQContainerInitializer;
import io.ngocnhan_tran1996.code.example.testcase.TestCase;
import io.ngocnhan_tran1996.code.example.rabbitmq.BaseConfig;

@ActiveProfiles("replyAddress")
@SpringBootTest(classes = {BaseConfig.class, ReplyListenerConfig.class})
@EnableTestcontainers(RabbitMQContainerInitializer.class)
class ReplyAddressTest {

  @Autowired
  RabbitTemplate rabbitTemplate;

  @Autowired
  Queue queue;

  @Autowired
  Queue replyQueue;

  @TestCase("io.ngocnhan_tran1996.code.example.rabbitmq.reply.testcase.ReplyTestArguments#testMessagePropsArguments")
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