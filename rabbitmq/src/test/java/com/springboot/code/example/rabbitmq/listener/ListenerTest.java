package com.springboot.code.example.rabbitmq.listener;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import com.springboot.code.example.container.EnableTestcontainers;
import com.springboot.code.example.container.RabbitMQContainerInitializer;
import com.springboot.code.example.rabbitmq.BaseConfig;
import com.springboot.code.example.testcase.TestCase;

@SpringBootTest(classes = {BaseConfig.class, ListenerConfig.class})
@EnableTestcontainers(RabbitMQContainerInitializer.class)
class ListenerTest {

  @Autowired
  RabbitTemplate rabbitTemplate;

  @Autowired
  Listener listener;

  @Autowired
  Queue queue;

  @TestCase
  void testReceive(String input, String output, Class<Throwable> exClass) throws Exception {

    if (Objects.nonNull(exClass)) {

      assertThatExceptionOfType(exClass)
          .isThrownBy(() -> rabbitTemplate.convertAndSend(queue.getName(), input));
      return;
    }

    rabbitTemplate.convertAndSend(queue.getName(), input);
    this.listener.latch.await(100, TimeUnit.MILLISECONDS);
    assertThat(listener.receiveMsg).isEqualTo(output);
  }

}