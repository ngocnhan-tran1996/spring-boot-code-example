package com.springboot.code.example.rabbitmq.batch;

import static org.assertj.core.api.Assertions.assertThat;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.AfterEach;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import com.springboot.code.example.rabbitmq.EnableTestcontainers;
import com.springboot.code.example.testcase.TestCase;

@ActiveProfiles("annotation-batch")
@SpringBootTest
@EnableTestcontainers
@Import(BatchConfig.class)
class AnnotationBatchTest {

  @Autowired
  RabbitAdmin rabbitAdmin;

  @Autowired
  RabbitTemplate rabbitTemplate;

  @Autowired
  Queue batchQueue;

  @Autowired
  AnnotationBatch annotationBatch;

  final CountDownLatch latch = new CountDownLatch(1);

  @TestCase("com.springboot.code.example.rabbitmq.batch.testcase.BatchTestArguments#testBatchArguments")
  void testAnnotationBatch(
      List<String> input,
      int expectedOutput)
      throws Exception {

    String queueName = batchQueue.getName();
    input.forEach(msg -> rabbitTemplate.convertAndSend(queueName, msg));

    latch.await(500, TimeUnit.MILLISECONDS);
    assertThat(annotationBatch.receiveMsg).hasSize(10);
    annotationBatch.receiveMsg.clear();

    latch.await(500, TimeUnit.MILLISECONDS);
    if (input.size() > 10) {

      assertThat(annotationBatch.receiveMsg).containsExactly("Even Msg 11");
    }
    assertThat(annotationBatch.receiveMsg).hasSize(expectedOutput);
    annotationBatch.receiveMsg.clear();
  }

  @AfterEach
  void tearDown() {

    rabbitAdmin.purgeQueue(batchQueue.getName());
  }

}