package com.springboot.code.example.rabbitmq.create.queue;

import java.util.List;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.ListenerContainerConsumerFailedEvent;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.context.event.EventListener;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class RabbitMQCreator {

  private static final String PREFIX_RECREATE_QUEUE = "local_QUEUE_TEST";
  private final RabbitAdmin rabbitAdmin;
  private final RabbitTemplate rabbitTemplate;

  public void execute() {

    List.of("local_QUEUE_TEST_3", "local_QUEUE_TEST_4")
        .forEach(queue -> rabbitAdmin.declareQueue(new Queue(queue)));

    for (int i = 0; i < 5; i++) {
      String queue = i == 0
          ? PREFIX_RECREATE_QUEUE
          : "local_QUEUE_TEST_" + i;
      rabbitTemplate.convertAndSend(queue, "Message from queue " + i);
    }
  }

  @EventListener
  public void events(ListenerContainerConsumerFailedEvent event) {

    SimpleMessageListenerContainer container = (SimpleMessageListenerContainer) event.getSource();
    String queueName = container.getQueueNames()[0];
    if (rabbitAdmin.getQueueProperties(queueName) == null) {
      this.execute();
    }

  }

}
