package io.ngocnhan_tran1996.code.example.rabbitmq.reply;

import org.springframework.amqp.core.AnonymousQueue;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;

@TestConfiguration
class ReplyListenerConfig {

  @Bean
  Queue replyQueue() {

    return new AnonymousQueue();
  }

  @Profile("!send-to")
  @Bean
  ReplyListener listener() {

    return new ReplyListener.Reply();
  }

  @Profile("send-to")
  @Bean
  ReplyListener sendListener() {

    return new ReplyListener.Send();
  }

  @Profile("replyAddress")
  @Bean
  RabbitTemplate replyRabbitTemplate(ConnectionFactory connectionFactory) {

    var template = new RabbitTemplate(connectionFactory);
    template.setReplyAddress(replyQueue().getName());
    template.setUseDirectReplyToContainer(false);
    return template;
  }

  @Profile("replyAddress")
  @Bean
  SimpleMessageListenerContainer messageListenerContainer(ConnectionFactory connectionFactory) {

    var container = new SimpleMessageListenerContainer(connectionFactory);
    container.setQueues(replyQueue());
    container.setMessageListener(replyRabbitTemplate(connectionFactory));
    return container;
  }

}