package io.ngocnhan_tran1996.code.example.rabbitmq.consumer;

import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Queue;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;

@TestConfiguration
class ConsumerConfig {

    @Profile("event")
    @Bean
    ConsumerListener consumerListener() {

        return new ConsumerListener();
    }

    @Profile("event")
    @Bean
    ConsumerEvent consumerEvent(AmqpAdmin amqpAdmin) {

        return new ConsumerEvent(amqpAdmin);
    }

    @Profile("byId")
    @Bean
    ConsumerByIdListener consumerByIdListener() {

        return new ConsumerByIdListener();
    }

    @Profile("byId")
    @Bean
    Queue queue() {

        return new Queue(ConsumerByIdListener.NEW_QUEUE_NAME);
    }

}