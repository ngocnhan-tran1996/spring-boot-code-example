package io.ngocnhan_tran1996.code.example.rabbitmq.prefetch;

import static org.assertj.core.api.Assertions.assertThat;

import io.ngocnhan_tran1996.code.example.container.EnableTestcontainers;
import io.ngocnhan_tran1996.code.example.container.RabbitMQContainerInitializer;
import io.ngocnhan_tran1996.code.example.rabbitmq.BaseConfig;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = {BaseConfig.class, PrefetchConfig.class})
@EnableTestcontainers(RabbitMQContainerInitializer.class)
class PrefetchTest {

    @Autowired
    RabbitAdmin rabbitAdmin;

    @Autowired
    RabbitTemplate rabbitTemplate;

    @Autowired
    Queue queue;

    @Autowired
    PrefetchListener prefetchListener;

    @BeforeEach
    void init() {

        IntStream.rangeClosed(1, 300)
            .boxed()
            .forEach(i -> rabbitTemplate.convertAndSend(queue.getName(), "Hello world " + i));
    }

    @Test
    void testPrefetch() throws Exception {

        this.prefetchListener.latch.await(100, TimeUnit.MILLISECONDS);
        assertThat(rabbitAdmin.getQueueInfo(queue.getName()).getMessageCount())
            .isEqualTo(49);
    }

}