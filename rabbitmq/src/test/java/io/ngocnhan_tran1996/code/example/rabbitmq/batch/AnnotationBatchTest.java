package io.ngocnhan_tran1996.code.example.rabbitmq.batch;

import static org.assertj.core.api.Assertions.assertThat;

import io.ngocnhan_tran1996.code.example.container.EnableTestcontainers;
import io.ngocnhan_tran1996.code.example.container.RabbitMQContainerInitializer;
import io.ngocnhan_tran1996.code.example.rabbitmq.BaseConfig;
import io.ngocnhan_tran1996.code.example.testcase.TestCase;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.AfterEach;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("annotation-batch")
@SpringBootTest(classes = {BaseConfig.class, BatchConfig.class})
@EnableTestcontainers(RabbitMQContainerInitializer.class)
class AnnotationBatchTest {

    final CountDownLatch latch = new CountDownLatch(1);
    @Autowired
    RabbitAdmin rabbitAdmin;
    @Autowired
    RabbitTemplate rabbitTemplate;
    @Autowired
    Queue queue;
    @Autowired
    AnnotationBatchListener annotationBatchListener;

    @TestCase("io.ngocnhan_tran1996.code.example.rabbitmq.batch.testcase.BatchTestArguments#testBatchArguments")
    void testAnnotationBatch(
        List<String> input,
        int expectedOutput)
        throws Exception {

        String queueName = queue.getName();
        input.forEach(msg -> rabbitTemplate.convertAndSend(queueName, msg));

        latch.await(100, TimeUnit.MILLISECONDS);
        assertThat(annotationBatchListener.receiveMsg).hasSize(10);
        annotationBatchListener.receiveMsg.clear();

        latch.await(200, TimeUnit.MILLISECONDS);
        if (input.size() > 10) {

            assertThat(annotationBatchListener.receiveMsg).containsExactly("Even Msg 11");
        }
        assertThat(annotationBatchListener.receiveMsg).hasSize(expectedOutput);
        annotationBatchListener.receiveMsg.clear();
    }

    @AfterEach
    void tearDown() {

        rabbitAdmin.purgeQueue(queue.getName());
    }

}