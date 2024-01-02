package io.ngocnhan_tran1996.code.example.rabbitmq.json;

import java.util.List;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.testcontainers.shaded.com.fasterxml.jackson.core.JsonProcessingException;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j2;

@Log4j2
@RabbitListener(queues = "#{queue.name}", messageConverter = "jsonConverter")
class JsonListener {

  private static final ObjectMapper mapper = new ObjectMapper();

  @RabbitHandler
  void listen(Employee employee) throws JsonProcessingException {

    log.info("Employee message is {}", mapper.writeValueAsString(employee));
  }

  @RabbitHandler
  void listenBatch(List<Employee> employee) throws JsonProcessingException {

    log.info("Batch message is {}", mapper.writeValueAsString(employee));
  }

  @RabbitHandler
  void receive(Chairman chairman) throws JsonProcessingException {

    log.info("Chairman message is {}", mapper.writeValueAsString(chairman));
  }

}