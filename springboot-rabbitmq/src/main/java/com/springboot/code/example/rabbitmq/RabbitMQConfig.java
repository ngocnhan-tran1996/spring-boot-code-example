// package com.springboot.code.example.rabbitmq;
//
// import java.util.HashMap;
// import java.util.Map;
// import org.springframework.boot.autoconfigure.amqp.RabbitRetryTemplateCustomizer;
// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;
// import org.springframework.retry.policy.SimpleRetryPolicy;
//
// @Configuration
// public class RabbitMQConfig {
//
// // @Bean
// // SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(
// // ConnectionFactory connectionFactory,
// // SimpleRabbitListenerContainerFactoryConfigurer configurer,
// // RabbitProperties rabbitProperties) {
// // SimpleRabbitListenerContainerFactory factory =
// // new SimpleRabbitListenerContainerFactory();
// // configurer.configure(factory, connectionFactory);
// //
// // Advice[] t = Arrays.copyOf(factory.getAdviceChain(), factory.getAdviceChain().length + 1);
// // t[factory.getAdviceChain().length] = workMessagesRetryInterceptor(rabbitProperties);
// // factory.setAdviceChain(t);
// // return factory;
// // }
// //
// // @Bean
// // RetryOperationsInterceptor workMessagesRetryInterceptor(RabbitProperties rabbitProperties) {
// //
// // RabbitProperties.Template templateProperties = rabbitProperties.getTemplate();
// // RabbitProperties.Retry retry = templateProperties.getRetry();
// //
// // return org.springframework.amqp.rabbit.config.RetryInterceptorBuilder.stateless()
// // .retryPolicy(rejectionRetryPolicy())
// // .backOffOptions(retry.getInitialInterval().toMillis(), retry.getMultiplier(), retry
// // .getMaxInterval().toMillis())
// // .build();
// // }
// //
// @Bean
// SimpleRetryPolicy rejectionRetryPolicy() {
//
// Map<Class<? extends Throwable>, Boolean> exceptionsMap = new HashMap<>();
// exceptionsMap.put(NullPointerException.class, true); // retriable
// exceptionsMap.put(IllegalArgumentException.class, false);// not retriable
// return new SimpleRetryPolicy(3, exceptionsMap, true);
// }
//
// @Bean
// RabbitRetryTemplateCustomizer customizeRetryPolicy() {
//
// return (target, retryTemplate) -> retryTemplate.setRetryPolicy(rejectionRetryPolicy());
// }
//
// }