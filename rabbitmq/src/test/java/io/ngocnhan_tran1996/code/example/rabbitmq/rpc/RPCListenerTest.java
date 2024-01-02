package io.ngocnhan_tran1996.code.example.rabbitmq.rpc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import java.util.Objects;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import io.ngocnhan_tran1996.code.example.container.EnableTestcontainers;
import io.ngocnhan_tran1996.code.example.container.RabbitMQContainerInitializer;
import io.ngocnhan_tran1996.code.example.testcase.TestCase;
import io.ngocnhan_tran1996.code.example.rabbitmq.BaseConfig;

@SpringBootTest(classes = {BaseConfig.class, RPCListenerConfig.class})
@EnableTestcontainers(RabbitMQContainerInitializer.class)
class RPCListenerTest {

  @Autowired
  RabbitTemplate rabbitTemplate;

  @Autowired
  RPCListener listener;

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