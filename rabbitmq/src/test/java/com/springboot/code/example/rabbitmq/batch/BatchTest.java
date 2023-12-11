package com.springboot.code.example.rabbitmq.batch;

import static org.assertj.core.api.Assertions.assertThat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.junit.jupiter.api.AfterEach;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.BatchingRabbitTemplate;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import com.springboot.code.example.rabbitmq.BaseConfig;
import com.springboot.code.example.rabbitmq.EnableTestcontainers;
import com.springboot.code.example.testcase.TestCase;

@ActiveProfiles("batch")
@SpringBootTest(classes = {BaseConfig.class, BatchConfig.class})
@EnableTestcontainers
class BatchTest {

  @Autowired
  RabbitAdmin rabbitAdmin;

  @Autowired
  BatchingRabbitTemplate batchingRabbitTemplate;

  @Autowired
  Queue queue;

  final CountDownLatch latch = new CountDownLatch(1);

  @TestCase
  void testBatch(
      List<String> input,
      int expectedOutput)
      throws Exception {

    String queueName = queue.getName();
    input.forEach(msg -> batchingRabbitTemplate.convertAndSend(queueName, msg));

    latch.await(100, TimeUnit.MILLISECONDS);
    var actualOutput = batchingRabbitTemplate.receive(queueName);
    assertThat(extractMessages(new String(actualOutput.getBody()))).hasSize(10);

    latch.await(200, TimeUnit.MILLISECONDS);
    var actualRemainedOutput = Optional.ofNullable(batchingRabbitTemplate.receive(queueName))
        .map(Message::getBody)
        .map(String::new)
        .map(this::extractMessages)
        .orElse(List.of());
    assertThat(actualRemainedOutput).hasSize(expectedOutput);
  }

  @AfterEach
  void tearDown() {

    rabbitAdmin.purgeQueue(queue.getName());
  }

  private List<String> extractMessages(final String text) {

    var messages = new ArrayList<String>();

    Pattern p = Pattern.compile("([A-z ]+[0-9]{1,2})");
    Matcher matcher = p.matcher(text);
    while (matcher.find()) {
      messages.add(matcher.group());
    }

    return messages;
  }

}