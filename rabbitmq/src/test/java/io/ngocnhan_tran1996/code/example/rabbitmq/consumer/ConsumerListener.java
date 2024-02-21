package io.ngocnhan_tran1996.code.example.rabbitmq.consumer;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.rabbit.annotation.RabbitListener;

@Log4j2
@RequiredArgsConstructor
class ConsumerListener {

    static final String QUEUE_NAME = "consumer.queue.test";

    @RabbitListener(queues = QUEUE_NAME)
    void receive(String msg) {

        log.debug("Receive {}", msg);
    }

}