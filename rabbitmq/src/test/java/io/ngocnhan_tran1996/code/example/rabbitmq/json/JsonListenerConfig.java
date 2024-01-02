package io.ngocnhan_tran1996.code.example.rabbitmq.json;

import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
class JsonListenerConfig {

  @Bean
  JsonListener listener() {

    return new JsonListener();
  }

  @Bean
  Jackson2JsonMessageConverter jsonConverter() {
    return new Jackson2JsonMessageConverter();
  }

}