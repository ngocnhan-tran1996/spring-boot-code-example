package io.ngocnhan_tran1996.code.example.rabbitmq.listener;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import io.ngocnhan_tran1996.code.example.container.EnableTestcontainers;
import io.ngocnhan_tran1996.code.example.container.RabbitMQContainerInitializer;
import io.ngocnhan_tran1996.code.example.rabbitmq.BaseConfig;
import io.ngocnhan_tran1996.code.example.testcase.TestCase;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = {BaseConfig.class, ListenerConfig.class})
@EnableTestcontainers(RabbitMQContainerInitializer.class)
class ListenerTest {

    @Autowired
    RabbitTemplate rabbitTemplate;

    @Autowired
    Listener listener;

    @Autowired
    Queue queue;

    @TestCase
    void testReceive(String input, String output, Class<Throwable> exClass) throws Exception {

        if (Objects.nonNull(exClass)) {

            assertThatExceptionOfType(exClass)
                .isThrownBy(() -> rabbitTemplate.convertAndSend(queue.getName(), input));
            return;
        }

        rabbitTemplate.convertAndSend(queue.getName(), input);
        this.listener.latch.await(100, TimeUnit.MILLISECONDS);
        assertThat(listener.receiveMsg).isEqualTo(output);
    }

}