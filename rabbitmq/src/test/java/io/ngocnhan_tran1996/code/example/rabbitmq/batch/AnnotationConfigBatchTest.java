package io.ngocnhan_tran1996.code.example.rabbitmq.batch;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.atMost;
import static org.mockito.Mockito.verify;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.AfterEach;
import org.mockito.Mock;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.BatchingRabbitTemplate;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import io.ngocnhan_tran1996.code.example.container.EnableTestcontainers;
import io.ngocnhan_tran1996.code.example.container.RabbitMQContainerInitializer;
import io.ngocnhan_tran1996.code.example.testcase.TestCase;
import io.ngocnhan_tran1996.code.example.rabbitmq.BaseConfig;

@ActiveProfiles("config-batch")
@SpringBootTest(classes = {BaseConfig.class, BatchConfig.class})
@EnableTestcontainers(RabbitMQContainerInitializer.class)
class AnnotationConfigBatchTest {

  @Autowired
  RabbitAdmin rabbitAdmin;

  @Autowired
  BatchingRabbitTemplate batchingRabbitTemplate;

  @Autowired
  Queue queue;

  @Autowired
  AnnotationBatchListener annotationBatchListener;

  @Mock
  org.apache.logging.log4j.Logger log;

  final CountDownLatch latch = new CountDownLatch(1);

  @TestCase("io.ngocnhan_tran1996.code.example.rabbitmq.batch.testcase.BatchTestArguments#testBatchArguments")
  void testAnnotationBatch(
      List<String> input,
      int expectedOutput)
      throws Exception {

    String queueName = queue.getName();
    input.forEach(msg -> batchingRabbitTemplate.convertAndSend(queueName, msg));

    latch.await(200, TimeUnit.MILLISECONDS);
    assertThat(annotationBatchListener.receiveMsg).hasSize(input.size());
    this.annotationBatchListener.receiveMsg.clear();
    verify(log, atMost(expectedOutput + 1)).info("Hello, world");
  }

  @AfterEach
  void tearDown() {

    rabbitAdmin.purgeQueue(queue.getName());
  }

}