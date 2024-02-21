package io.ngocnhan_tran1996.code.example.rabbitmq.prefetch;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.rabbit.annotation.RabbitListener;

@Log4j2
class PrefetchListener {

    final CountDownLatch latch = new CountDownLatch(1);

    @RabbitListener(queues = "#{queue.name}", containerFactory = "roundRobinContainerFactory")
    void consume1(String msg) throws InterruptedException {

        log.info("Consumer 1 : {}", msg);
        latch.await(1, TimeUnit.MINUTES);
    }

    @RabbitListener(queues = "#{queue.name}", containerFactory = "fairContainerFactory")
    void consume2(String msg) throws InterruptedException {

        log.info("Consumer 2 : {}", msg);
        latch.await(1, TimeUnit.MINUTES);
    }

}