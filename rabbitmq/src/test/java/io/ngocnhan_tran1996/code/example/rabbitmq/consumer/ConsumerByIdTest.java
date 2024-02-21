package io.ngocnhan_tran1996.code.example.rabbitmq.consumer;

import static org.assertj.core.api.Assertions.assertThat;

import io.ngocnhan_tran1996.code.example.container.EnableTestcontainers;
import io.ngocnhan_tran1996.code.example.container.RabbitMQContainerInitializer;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.test.RabbitListenerTest;
import org.springframework.amqp.rabbit.test.RabbitListenerTestHarness;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("byId")
@SpringBootTest(classes = ConsumerConfig.class)
@EnableTestcontainers(RabbitMQContainerInitializer.class)
@RabbitListenerTest(spy = false, capture = true)
class ConsumerByIdTest {

    final CountDownLatch latch = new CountDownLatch(1);
    @Autowired
    RabbitAdmin rabbitAdmin;
    @Autowired
    RabbitTemplate rabbitTemplate;
    @Autowired
    RabbitListenerTestHarness harness;

    @Test
    void testListenNewQueue() throws Exception {

        String queueName = "local_test_queue";
        String msg = "Hello, world!";

        rabbitTemplate.convertAndSend(ConsumerByIdListener.NEW_QUEUE_NAME, queueName);
        latch.await(100, TimeUnit.MILLISECONDS);

        rabbitTemplate.convertAndSend(queueName, msg);
        latch.await(100, TimeUnit.MILLISECONDS);

        var invocationData = harness.getNextInvocationDataFor(
            ConsumerByIdListener.LOCAL,
            0,
            TimeUnit.SECONDS);
        Object[] args = invocationData.getArguments();
        assertThat(args[0]).isEqualTo(msg);

        this.rabbitAdmin.deleteQueue(queueName);
        this.rabbitAdmin.deleteQueue(ConsumerByIdListener.NEW_QUEUE_NAME);
    }

}