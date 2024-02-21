package io.ngocnhan_tran1996.code.example.rabbitmq.listener;

import java.util.concurrent.CountDownLatch;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;

class Listener {

    final CountDownLatch latch = new CountDownLatch(1);
    String receiveMsg = "";

    @RabbitListener(queues = "#{queue.name}")
    void receive(@Payload String msg, @Header(AmqpHeaders.CONSUMER_QUEUE) String queueName) {

        this.receiveMsg = msg;
    }

}