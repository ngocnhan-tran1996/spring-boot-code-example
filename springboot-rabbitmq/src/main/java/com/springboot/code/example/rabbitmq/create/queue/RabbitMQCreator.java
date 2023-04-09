package com.springboot.code.example.rabbitmq.create.queue;

import java.util.List;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.listener.ListenerContainerConsumerFailedEvent;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.context.event.EventListener;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class RabbitMQCreator {

  private static final String RECREATE_QUEUE = "local_QUEUE_TEST_5";
  private final RabbitAdmin rabbitAdmin;

  public void execute() {

    List.of(RECREATE_QUEUE, "local_QUEUE_TEST_6")
        .forEach(queue -> rabbitAdmin.declareQueue(new Queue(queue, true)));
  }

  @EventListener
  public void events(ListenerContainerConsumerFailedEvent event) {

    SimpleMessageListenerContainer container = (SimpleMessageListenerContainer) event.getSource();
    String queueName = container.getQueueNames()[0];
    if (RECREATE_QUEUE.equals(queueName)) {
      this.rabbitAdmin.declareQueue(new Queue(queueName));
    }

    // or
    this.execute();
  }

}
