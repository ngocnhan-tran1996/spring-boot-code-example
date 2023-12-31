package com.springboot.code.example.rabbitmq.validation;

import static org.assertj.core.api.Assertions.assertThat;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import com.springboot.code.example.container.EnableTestcontainers;
import com.springboot.code.example.container.RabbitMQContainerInitializer;
import com.springboot.code.example.rabbitmq.BaseConfig;

@SpringBootTest(classes = {BaseConfig.class, ValidationConfig.class})
@EnableTestcontainers(RabbitMQContainerInitializer.class)
class ValidationTest {

  @Autowired
  RabbitAdmin rabbitAdmin;

  @Autowired
  RabbitTemplate rabbitTemplate;

  @Autowired
  Queue queue;

  final CountDownLatch latch = new CountDownLatch(1);

  @Test
  void testValidation() throws Exception {

    rabbitTemplate.convertAndSend(queue.getName(), new Employee());
    this.latch.await(100, TimeUnit.MILLISECONDS);
    assertThat(rabbitAdmin.getQueueInfo(queue.getName()).getMessageCount())
        .isEqualTo(0);
  }

}
