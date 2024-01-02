package io.ngocnhan_tran1996.code.example.rabbitmq.validation;

import org.springframework.amqp.rabbit.annotation.RabbitListenerConfigurer;
import org.springframework.amqp.rabbit.listener.RabbitListenerEndpointRegistrar;
import org.springframework.amqp.rabbit.listener.api.RabbitListenerErrorHandler;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

@TestConfiguration
class ValidationConfig implements RabbitListenerConfigurer {

  @Autowired
  private LocalValidatorFactoryBean validator;

  @Override
  public void configureRabbitListeners(RabbitListenerEndpointRegistrar registrar) {
    registrar.setValidator(this.validator);
  }

  @Bean
  ValidationConsumer validationConsumer() {

    return new ValidationConsumer();
  }

  @Bean
  Jackson2JsonMessageConverter jsonConverter() {
    return new Jackson2JsonMessageConverter();
  }

  @Bean
  RabbitListenerErrorHandler validationErrorHandler() {
    return (message, msg, exception) -> {

      msg.getHeaders().get(AmqpHeaders.CHANNEL, com.rabbitmq.client.Channel.class)
          .basicReject(msg.getHeaders().get(AmqpHeaders.DELIVERY_TAG, Long.class), false);
      return null;
    };
  }

}