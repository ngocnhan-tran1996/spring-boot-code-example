package io.ngocnhan_tran1996.code.example.transaction.rabbitmq;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import io.ngocnhan_tran1996.code.example.container.EnableTestcontainers;
import io.ngocnhan_tran1996.code.example.container.RabbitMQContainerInitializer;
import io.ngocnhan_tran1996.code.example.testcase.TestCase;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;

@EnableAutoConfiguration(exclude = DataSourceAutoConfiguration.class)
@SpringBootTest(classes = {BaseConfig.class, TransactionConfig.class})
@EnableTestcontainers(RabbitMQContainerInitializer.class)
class TransactionTest {

    @Autowired
    RabbitAdmin rabbitAdmin;

    @Autowired
    Queue queue;

    @Autowired
    TransactionProducer transactionProducer;

    @TestCase
    void testSend(boolean input, int output) throws Exception {

        assertThatExceptionOfType(RuntimeException.class)
            .isThrownBy(() -> transactionProducer.send(input));
        var msgCount = rabbitAdmin.getQueueInfo(queue.getName())
            .getMessageCount();
        assertThat(msgCount).isEqualTo(output);
    }

}