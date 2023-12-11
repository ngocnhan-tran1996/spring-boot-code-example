package com.springboot.code.example.rabbitmq.consumer;

import static org.assertj.core.api.Assertions.assertThat;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.core.QueueInformation;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import com.springboot.code.example.rabbitmq.EnableTestcontainers;

@SpringBootTest(classes = ConsumerConfig.class)
@EnableTestcontainers
class ConsumerTest {

  @Autowired
  RabbitAdmin rabbitAdmin;

  @Autowired
  RabbitTemplate rabbitTemplate;

  final CountDownLatch latch = new CountDownLatch(1);

  @Test
  void testConsume() throws Exception {

    // auto create queue
    rabbitTemplate.convertAndSend(ConsumerListener.QUEUE_NAME, "Test");
    var queueInfo = rabbitAdmin.getQueueInfo(ConsumerListener.QUEUE_NAME);
    assertThat(queueInfo.getConsumerCount()).isEqualTo(1);
    assertThat(queueInfo.getMessageCount()).isZero();

    // delete queue by manual
    rabbitAdmin.deleteQueue(ConsumerListener.QUEUE_NAME);
    queueInfo = rabbitAdmin.getQueueInfo(ConsumerListener.QUEUE_NAME);
    assertThat(queueInfo).isNull();

    latch.await(1, TimeUnit.SECONDS);

    // auto re-create queue
    queueInfo = rabbitAdmin.getQueueInfo(ConsumerListener.QUEUE_NAME);
    assertThat(queueInfo)
        .usingRecursiveComparison()
        .isEqualTo(new QueueInformation(ConsumerListener.QUEUE_NAME, 0, 0));
  }

}