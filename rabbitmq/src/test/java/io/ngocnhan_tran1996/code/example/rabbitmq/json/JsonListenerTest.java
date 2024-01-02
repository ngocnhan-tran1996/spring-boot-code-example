package io.ngocnhan_tran1996.code.example.rabbitmq.json;

import static org.assertj.core.api.Assertions.assertThat;
import java.util.List;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import io.ngocnhan_tran1996.code.example.container.EnableTestcontainers;
import io.ngocnhan_tran1996.code.example.container.RabbitMQContainerInitializer;
import io.ngocnhan_tran1996.code.example.testcase.TestCase;
import io.ngocnhan_tran1996.code.example.rabbitmq.BaseConfig;

@SpringBootTest(classes = {BaseConfig.class, JsonListenerConfig.class})
@EnableTestcontainers(RabbitMQContainerInitializer.class)
class JsonListenerTest {

  @Autowired
  RabbitAdmin rabbitAdmin;

  @Autowired
  RabbitTemplate rabbitTemplate;

  @Autowired
  JsonListener jsonListener;

  @Autowired
  Queue queue;

  @TestCase
  void testListen(Employee input, int output) throws Exception {

    rabbitTemplate.convertAndSend(queue.getName(), input);
    var msgCount = rabbitAdmin.getQueueInfo(queue.getName())
        .getMessageCount();
    assertThat(msgCount).isEqualTo(output);
  }

  @TestCase
  void testListenBatch(List<Employee> input, int output) throws Exception {

    rabbitTemplate.convertAndSend(queue.getName(), input);
    var msgCount = rabbitAdmin.getQueueInfo(queue.getName())
        .getMessageCount();
    assertThat(msgCount).isEqualTo(output);
  }

  @TestCase
  void testReceive(Chairman input, int output) throws Exception {

    rabbitTemplate.convertAndSend(queue.getName(), input);
    var msgCount = rabbitAdmin.getQueueInfo(queue.getName())
        .getMessageCount();
    assertThat(msgCount).isEqualTo(output);
  }

}