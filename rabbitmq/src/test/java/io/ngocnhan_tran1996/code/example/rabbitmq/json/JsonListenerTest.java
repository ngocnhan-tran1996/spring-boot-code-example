package io.ngocnhan_tran1996.code.example.rabbitmq.json;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.amqp.support.converter.AbstractJavaTypeMapper.DEFAULT_CONTENT_CLASSID_FIELD_NAME;
import static org.springframework.amqp.support.converter.DefaultClassMapper.DEFAULT_CLASSID_FIELD_NAME;
import java.util.List;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import io.ngocnhan_tran1996.code.example.container.EnableTestcontainers;
import io.ngocnhan_tran1996.code.example.container.RabbitMQContainerInitializer;
import io.ngocnhan_tran1996.code.example.rabbitmq.BaseConfig;
import io.ngocnhan_tran1996.code.example.testcase.TestCase;

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

    rabbitTemplate.convertAndSend(queue.getName(), input, message -> {

      message.getMessageProperties()
          .setHeader(DEFAULT_CLASSID_FIELD_NAME, Employee.class.getName());
      return message;
    });
    var msgCount = rabbitAdmin.getQueueInfo(queue.getName())
        .getMessageCount();
    assertThat(msgCount).isEqualTo(output);
  }

  @TestCase
  void testListenBatch(List<Employee> input, int output) throws Exception {

    rabbitTemplate.convertAndSend(queue.getName(), input, message -> {

      var props = message.getMessageProperties();
      props.setHeader(DEFAULT_CLASSID_FIELD_NAME, List.class.getName());
      props.setHeader(DEFAULT_CONTENT_CLASSID_FIELD_NAME, Employee.class.getName());
      return message;
    });
    var msgCount = rabbitAdmin.getQueueInfo(queue.getName())
        .getMessageCount();
    assertThat(msgCount).isEqualTo(output);
  }

  @TestCase
  void testReceive(Chairman input, int output) throws Exception {

    rabbitTemplate.convertAndSend(queue.getName(), input, message -> {

      message.getMessageProperties()
          .setHeader(DEFAULT_CLASSID_FIELD_NAME, Chairman.class.getName());
      return message;
    });
    var msgCount = rabbitAdmin.getQueueInfo(queue.getName())
        .getMessageCount();
    assertThat(msgCount).isEqualTo(output);
  }

}