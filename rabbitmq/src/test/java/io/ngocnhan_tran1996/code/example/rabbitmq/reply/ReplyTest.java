package io.ngocnhan_tran1996.code.example.rabbitmq.reply;

import static org.assertj.core.api.Assertions.assertThat;

import io.ngocnhan_tran1996.code.example.container.EnableTestcontainers;
import io.ngocnhan_tran1996.code.example.container.RabbitMQContainerInitializer;
import io.ngocnhan_tran1996.code.example.rabbitmq.BaseConfig;
import io.ngocnhan_tran1996.code.example.testcase.TestCase;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = {BaseConfig.class, ReplyListenerConfig.class})
@EnableTestcontainers(RabbitMQContainerInitializer.class)
class ReplyTest {

    final CountDownLatch latch = new CountDownLatch(1);
    @Autowired
    RabbitTemplate rabbitTemplate;
    @Autowired
    Queue queue;
    @Autowired
    Queue replyQueue;

    @TestCase
    void testMessageProps(
        String input,
        String expectedOutput)
        throws Exception {

        this.rabbitTemplate.convertAndSend(queue.getName(), input, message -> {

            message.getMessageProperties().setReplyTo(replyQueue.getName());
            message.getMessageProperties().setCorrelationId(UUID.randomUUID().toString());
            return message;
        });
        latch.await(100, TimeUnit.MILLISECONDS);

        String actualOutput = (String) this.rabbitTemplate.receiveAndConvert(replyQueue.getName());
        assertThat(actualOutput).isEqualTo(expectedOutput);
    }

}