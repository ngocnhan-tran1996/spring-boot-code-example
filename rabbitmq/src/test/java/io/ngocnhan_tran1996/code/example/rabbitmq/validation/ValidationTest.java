package io.ngocnhan_tran1996.code.example.rabbitmq.validation;

import static org.assertj.core.api.Assertions.assertThat;

import io.ngocnhan_tran1996.code.example.container.EnableTestcontainers;
import io.ngocnhan_tran1996.code.example.container.RabbitMQContainerInitializer;
import io.ngocnhan_tran1996.code.example.rabbitmq.BaseConfig;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = {BaseConfig.class, ValidationConfig.class})
@EnableTestcontainers(RabbitMQContainerInitializer.class)
class ValidationTest {

    final CountDownLatch latch = new CountDownLatch(1);
    @Autowired
    RabbitAdmin rabbitAdmin;
    @Autowired
    RabbitTemplate rabbitTemplate;
    @Autowired
    Queue queue;

    @Test
    void testValidation() throws Exception {

        rabbitTemplate.convertAndSend(queue.getName(), new Employee());
        this.latch.await(100, TimeUnit.MILLISECONDS);
        assertThat(rabbitAdmin.getQueueInfo(queue.getName()).getMessageCount())
            .isZero();
    }

}