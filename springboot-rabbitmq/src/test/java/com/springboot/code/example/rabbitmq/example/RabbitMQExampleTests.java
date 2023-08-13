package com.springboot.code.example.rabbitmq.example;

import static org.assertj.core.api.Assertions.assertThat;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;

@SpringBootTest
@ExtendWith(OutputCaptureExtension.class)
class RabbitMQExampleTests {

  @Autowired
  RabbitMQExample rabbitMQExample;

  @Autowired
  RabbitTemplate rabbitTemplate;

  CountDownLatch latch = new CountDownLatch(1);

  @Test
  void testListenNewQueue(CapturedOutput capturedOutput) throws Exception {

    rabbitTemplate.convertAndSend("local_CREATE_QUEUE", "local_test_queue");
    rabbitTemplate.convertAndSend("local_test_queue", "test");
    latch.await(1, TimeUnit.SECONDS);

    assertThat(capturedOutput.getOut()).contains(
        "[1;33mc.s.c.e.r.e.RabbitMQExample[m: Message: test");
  }

}
