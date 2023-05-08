package com.springboot.code.example.elastic;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.amqp.SimpleRabbitListenerContainerFactoryConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.springboot.code.example.common.helper.Strings;
import com.springboot.code.example.elastic.consumer.MessageConsumer;

@Configuration
public class ElasticApplicationConfig {

  @Value("${app.queue.name-listener}")
  String queueName;

  @Bean
  Queue queue() {
    return new Queue(queueName);
  }

  @Bean
  SimpleMessageListenerContainer simpleMessageListenerContainer(
      SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory,
      ConnectionFactory connectionFactory,
      MessageListenerAdapter messageListenerAdapter) {

    var container = rabbitListenerContainerFactory.createListenerContainer();
    container.setConnectionFactory(connectionFactory);
    container.addAfterReceivePostProcessors(
        message -> {

          var messageProperties = message.getMessageProperties();
          if (Strings.isBlank(messageProperties.getReceivedExchange())) {
            messageProperties.setReceivedExchange(queueName);
          }
          return message;
        });
    container.setQueueNames(queueName);
    container.setMessageListener(messageListenerAdapter);
    return container;
  }

  @Bean
  MessageListenerAdapter messageListenerAdapter(MessageConsumer messageConsumer) {
    var adapter = new MessageListenerAdapter(messageConsumer, "receiveListener");
    adapter.setMessageConverter(null);
    return adapter;
  }

  @Bean
  SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(
      SimpleRabbitListenerContainerFactoryConfigurer configurer,
      ConnectionFactory connectionFactory) {
    SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
    factory.setConnectionFactory(connectionFactory);
    factory.setAfterReceivePostProcessors(
        message -> {

          var messageProperties = message.getMessageProperties();
          if (Strings.isBlank(messageProperties.getReceivedExchange())) {
            messageProperties.setReceivedExchange(messageProperties.getConsumerQueue());
          }
          return message;
        });

    configurer.configure(factory, connectionFactory);
    return factory;
  }

}