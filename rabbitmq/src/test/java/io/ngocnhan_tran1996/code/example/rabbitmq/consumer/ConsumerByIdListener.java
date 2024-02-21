package io.ngocnhan_tran1996.code.example.rabbitmq.consumer;

import java.util.Arrays;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.listener.RabbitListenerEndpointRegistry;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.beans.factory.annotation.Autowired;

@Log4j2
@RequiredArgsConstructor
class ConsumerByIdListener {

    static final String NEW_QUEUE_NAME = "new.queue.test";
    static final String LOCAL = "local";

    @Autowired
    RabbitListenerEndpointRegistry rabbitListenerEndpointRegistry;

    @Autowired
    AmqpAdmin amqpAdmin;

    @RabbitListener(queues = NEW_QUEUE_NAME)
    void listenNewQueue(String newQueue) {
        var container = (SimpleMessageListenerContainer) rabbitListenerEndpointRegistry
            .getListenerContainer(LOCAL);

        // avoid existed queue
        if (Arrays.binarySearch(container.getQueueNames(), newQueue) == -1) {

            log.info("Create queue {}", newQueue);
            amqpAdmin.declareQueue(new Queue(newQueue));
            container.addQueueNames(newQueue);
        }
    }

    @RabbitListener(id = LOCAL)
    void listenQueueById(String message) {

        log.info("Message: {}", message);
    }

}