package com.springboot.code.example.rabbitmq.listener;

import static org.assertj.core.api.Assertions.assertThat;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.params.ParameterizedTest;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import com.springboot.code.example.TestCase;
import com.springboot.code.example.rabbitmq.AbstractIntegrationTest;
import lombok.extern.log4j.Log4j2;

@SpringBootTest
class ListenerTest extends AbstractIntegrationTest {

  @Autowired
  RabbitAdmin rabbitAdmin;

  @Autowired
  RabbitTemplate rabbitTemplate;

  @Autowired
  Listener listener;

  @Autowired
  Queue q1;

  @ParameterizedTest
  @TestCase
  void testListener(String input, String output) throws Exception {

    rabbitTemplate.convertAndSend(q1.getName(), input);
    listener.latch.await(100, TimeUnit.MILLISECONDS);
    assertThat(listener.receiveMsg).isEqualTo(output);
  }

  @TestConfiguration
  static class Config {

    @Bean
    Queue q1() {

      return new Queue("listen.queue", false, true, true);
    }

    @Bean
    Listener listener() {
      return new Listener();
    }

  }

  @Log4j2
  static class Listener {

    String receiveMsg = "";
    CountDownLatch latch = new CountDownLatch(1);

    @RabbitListener(queues = "#{q1.name}")
    void foo(@Payload String msg, @Header(AmqpHeaders.CONSUMER_QUEUE) String queueName) {

      log.info("Receive {} from queue {}", msg, queueName);
      this.receiveMsg = msg;
    }

  }

}
