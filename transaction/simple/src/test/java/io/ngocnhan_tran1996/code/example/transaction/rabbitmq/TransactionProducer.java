package io.ngocnhan_tran1996.code.example.transaction.rabbitmq;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@RequiredArgsConstructor
class TransactionProducer {

    private final Queue queue;
    private final RabbitTemplate rabbitTemplate;

    public void send(boolean hasTransaction) {

        rabbitTemplate.setChannelTransacted(hasTransaction);
        this.rabbitTemplate.convertAndSend(queue.getName(), "test");
        throw new RuntimeException();
    }

}