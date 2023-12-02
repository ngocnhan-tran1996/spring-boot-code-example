package com.springboot.code.example.rabbitmq.listener;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import java.util.Objects;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import org.springframework.amqp.core.AnonymousQueue;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import com.springboot.code.example.rabbitmq.AbstractIntegrationTest;
import com.springboot.code.example.testcase.TestCase;

@SpringBootTest
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

  @TestConfiguration
  static class ListenerConfig {

    @Bean
    Queue anonymousQueue() {

      return new AnonymousQueue();
    }

    @Bean
    Queue rpcQueue() {

      return new AnonymousQueue();
    }

    @Bean
    Listener listener() {

      return new Listener();
    }

  }

  static class Listener {

    String receiveMsg = "";
    CountDownLatch latch = new CountDownLatch(1);

    @RabbitListener(queues = "#{anonymousQueue.name}")
    void receive(@Payload String msg, @Header(AmqpHeaders.CONSUMER_QUEUE) String queueName) {

      this.receiveMsg = msg;
    }

    @RabbitListener(queues = "#{rpcQueue.name}")
    String rpc(String msg) {

      return msg + "_RPC";
    }

  }

}
