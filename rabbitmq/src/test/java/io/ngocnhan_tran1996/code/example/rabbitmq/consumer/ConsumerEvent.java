package io.ngocnhan_tran1996.code.example.rabbitmq.consumer;

import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.listener.ConsumeOkEvent;
import org.springframework.amqp.rabbit.listener.MissingQueueEvent;
import org.springframework.context.event.EventListener;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
class ConsumerEvent {

  private final AmqpAdmin amqpAdmin;

  @EventListener
  void consumeOkEvent(ConsumeOkEvent event) {

    this.createQueue(event.getQueue());

  }

  @EventListener
  void missingQueueEvent(MissingQueueEvent event) {

    this.createQueue(event.getQueue());

  }

  private void createQueue(String queueName) {

    if (amqpAdmin.getQueueProperties(queueName) == null) {
      // for anonymous queue
      amqpAdmin.declareQueue(new Queue(queueName, false, true, true));
    }

  }

}