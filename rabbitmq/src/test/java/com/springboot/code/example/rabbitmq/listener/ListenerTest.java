package com.springboot.code.example.rabbitmq.listener;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import com.springboot.code.example.rabbitmq.AbstractIntegrationTest;
import com.springboot.code.example.testcase.TestCase;

@SpringBootTest
@Import(ListenerConfig.class)
class ListenerTest extends AbstractIntegrationTest {

  @Autowired
  RabbitTemplate rabbitTemplate;

  @Autowired
  Listener listener;

  @Autowired
  Queue anonymousQueue;

  @Autowired
  Queue rpcQueue;

  @TestCase
  void testReceive(String input, String output, Class<Throwable> exClass) throws Exception {

    if (Objects.nonNull(exClass)) {

      assertThatExceptionOfType(exClass)
          .isThrownBy(() -> rabbitTemplate.convertAndSend(anonymousQueue.getName(), input));
      return;
    }

    rabbitTemplate.convertAndSend(anonymousQueue.getName(), input);
    listener.latch.await(100, TimeUnit.MILLISECONDS);
    assertThat(listener.receiveMsg).isEqualTo(output);
  }

  @TestCase
  void testRPC(String input, String output, Class<Throwable> exClass) throws Exception {

    if (Objects.nonNull(exClass)) {

      assertThatExceptionOfType(exClass)
          .isThrownBy(() -> rabbitTemplate.convertSendAndReceive(anonymousQueue.getName(), input));
      return;
    }

    String actualOutput = (String) rabbitTemplate.convertSendAndReceive(
        rpcQueue.getName(),
        input);
    assertThat(actualOutput).isEqualTo(output);
  }

}