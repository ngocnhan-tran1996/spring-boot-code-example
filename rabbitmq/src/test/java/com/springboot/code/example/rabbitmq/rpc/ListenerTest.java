package com.springboot.code.example.rabbitmq.rpc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import java.util.Objects;
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
@Import({BaseConfig.class, ListenerConfig.class})
class ListenerTest {

  @Autowired
  RabbitTemplate rabbitTemplate;

  @Autowired
  Listener listener;

  @Autowired
  Queue queue;

  @TestCase
  void testRPC(String input, String output, Class<Throwable> exClass) throws Exception {

    if (Objects.nonNull(exClass)) {

      assertThatExceptionOfType(exClass)
          .isThrownBy(() -> rabbitTemplate.convertSendAndReceive(queue.getName(), input));
      return;
    }

    String actualOutput = (String) rabbitTemplate.convertSendAndReceive(
        queue.getName(),
        input);
    assertThat(actualOutput).isEqualTo(output);
  }

}