package io.ngocnhan_tran1996.code.example.rabbitmq.prefetch;

import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.boot.autoconfigure.amqp.SimpleRabbitListenerContainerFactoryConfigurer;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
class PrefetchConfig {

  @Bean("roundRobinContainerFactory")
  SimpleRabbitListenerContainerFactory roundRobinContainerFactory(
      SimpleRabbitListenerContainerFactoryConfigurer configurer,
      ConnectionFactory connectionFactory) {

    var factory = new SimpleRabbitListenerContainerFactory();
    configurer.configure(factory, connectionFactory);
    factory.setPrefetchCount(250);
    return factory;
  }

  @Bean("fairContainerFactory")
  SimpleRabbitListenerContainerFactory fairContainerFactory(
      SimpleRabbitListenerContainerFactoryConfigurer configurer,
      ConnectionFactory connectionFactory) {

    var factory = new SimpleRabbitListenerContainerFactory();
    configurer.configure(factory, connectionFactory);
    factory.setPrefetchCount(1);
    return factory;
  }

  @Bean
  PrefetchListener prefetchListener() {

    return new PrefetchListener();
  }

}