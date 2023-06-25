package com.springboot.code.example.transaction.rabbitmq.datasource.consumer;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.springboot.code.example.transaction.rabbitmq.datasource.constant.QueueNames;
import com.springboot.code.example.transaction.rabbitmq.datasource.entity.HistoryEntity;
import com.springboot.code.example.transaction.rabbitmq.datasource.repository.HistoryRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class QueueConsumer {

  private final HistoryRepository historyRepository;
  private final RabbitTemplate rabbitTemplate;

  @Transactional
  @RabbitListener(queuesToDeclare = @Queue(name = QueueNames.TRANSACTION_PRODUCER))
  public void listenInTransaction(Message message) {

    updateFailureData(message, QueueNames.TRANSACTION_CONSUMER);
  }

  @Transactional
  @RabbitListener(queuesToDeclare = @Queue(name = QueueNames.TRANSACTION_SAME_PRODUCER))
  public void listenInSameTransaction(Message message) {

    rabbitTemplate.setChannelTransacted(true);
    updateFailureData(message, QueueNames.TRANSACTION_SAME_CONSUMER);
  }

  @Transactional
  @RabbitListener(queuesToDeclare = @Queue(name = QueueNames.TRANSACTION_RELATIVE_PRODUCER))
  public void listenInSameRelativeTransaction(Message message) {

    updateFailureData(message, QueueNames.TRANSACTION_RELATIVE_CONSUMER);
  }

  @RabbitListener(queuesToDeclare = @Queue(name = QueueNames.OUT_TRANSACTION_PRODUCER))
  public void listenWithoutTransaction(Message message) {

    updateFailureData(message, QueueNames.OUT_TRANSACTION_CONSUMER);
  }

  private void updateFailureData(Message message, String queueName) {

    String body = new String(message.getBody());

    var historyEntity = new HistoryEntity();
    historyEntity.setStatus(body);
    historyRepository.save(historyEntity);

    rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());
    rabbitTemplate.convertAndSend(queueName, historyEntity);

    throw new IllegalArgumentException("Failure to save data");
  }

}
