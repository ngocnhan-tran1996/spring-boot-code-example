package io.ngocnhan_tran1996.code.example.transaction.datasource.rabbitmq;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.jdbc.JdbcTestUtils;
import io.ngocnhan_tran1996.code.example.container.EnableTestcontainers;
import io.ngocnhan_tran1996.code.example.container.PostgreSQLContainerInitializer;
import io.ngocnhan_tran1996.code.example.container.RabbitMQContainerInitializer;
import io.ngocnhan_tran1996.code.example.testcase.TestCase;
import io.ngocnhan_tran1996.code.example.transaction.domain.DogRepo;
import io.ngocnhan_tran1996.code.example.transaction.rabbitmq.BaseConfig;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TransactionRequiredException;

@ActiveProfiles("postgres")
@SpringBootTest(classes = {
    BaseConfig.class,
    DataSourceRabbitMQConfig.class
})
@EnableTestcontainers({PostgreSQLContainerInitializer.class, RabbitMQContainerInitializer.class})
@TestMethodOrder(OrderAnnotation.class)
class DataSourceRabbitMQTest {

  @Autowired
  RabbitAdmin rabbitAdmin;

  @Autowired
  Queue queue;

  @Autowired
  DataSourceRabbitMQConfigProducer producer;

  @Autowired
  DogRepo dogRepo;

  @Autowired
  JdbcTemplate jdbcTemplate;

  @Autowired
  EntityManager entityManager;

  @BeforeEach
  void init() {

    this.rabbitAdmin.purgeQueue(this.queue.getName());
    dogRepo.deleteAll();
  }

  @Order(1)
  @TestCase("io.ngocnhan_tran1996.code.example.transaction.rabbitmq.testcase.TransactionTestArguments#testSendArguments")
  void testSendWithJpa(boolean input, int output) throws Exception {

    assertThatExceptionOfType(RuntimeException.class)
        .isThrownBy(() -> producer.send(input));
    var msgCount = rabbitAdmin.getQueueInfo(queue.getName())
        .getMessageCount();
    assertThat(msgCount).isEqualTo(output);
    assertThat(dogRepo.findAll())
        .isEmpty();
  }

  @Order(2)
  @TestCase("io.ngocnhan_tran1996.code.example.transaction.rabbitmq.testcase.TransactionTestArguments#testSendWithCloneArguments")
  void testSendWithJpaWithoutTransaction(boolean input, int output) throws Exception {

    assertThatExceptionOfType(RuntimeException.class)
        .isThrownBy(() -> producer.sendWithoutTransaction(input));
    var msgCount = rabbitAdmin.getQueueInfo(queue.getName())
        .getMessageCount();
    assertThat(msgCount).isEqualTo(output);
    assertThat(dogRepo.findAll())
        .hasSize(output);
  }

  @Order(3)
  @TestCase("io.ngocnhan_tran1996.code.example.transaction.rabbitmq.testcase.TransactionTestArguments#testSendArguments")
  void testSendJdbc(boolean input, int output) throws Exception {

    assertThatExceptionOfType(RuntimeException.class)
        .isThrownBy(() -> producer.sendJdbc(input));
    var msgCount = rabbitAdmin.getQueueInfo(queue.getName())
        .getMessageCount();
    assertThat(msgCount).isEqualTo(output);
    assertThat(JdbcTestUtils.countRowsInTable(jdbcTemplate, "dog"))
        .isZero();
  }

  @Order(4)
  @TestCase("io.ngocnhan_tran1996.code.example.transaction.rabbitmq.testcase.TransactionTestArguments#testSendWithCloneArguments")
  void testSendJdbcWithoutTransaction(boolean input, int output) throws Exception {

    assertThatExceptionOfType(RuntimeException.class)
        .isThrownBy(() -> producer.sendJdbcWithoutTransaction(input));
    var msgCount = rabbitAdmin.getQueueInfo(queue.getName())
        .getMessageCount();
    assertThat(msgCount).isEqualTo(output);
    assertThat(JdbcTestUtils.countRowsInTable(jdbcTemplate, "dog"))
        .isEqualTo(output);
  }

  @Order(5)
  @TestCase("io.ngocnhan_tran1996.code.example.transaction.rabbitmq.testcase.TransactionTestArguments#testSendArguments")
  void testSendEntityManager(boolean input, int output) throws Exception {

    assertThatExceptionOfType(RuntimeException.class)
        .isThrownBy(() -> producer.sendEntityManager(input));
    var msgCount = rabbitAdmin.getQueueInfo(queue.getName())
        .getMessageCount();
    assertThat(msgCount).isEqualTo(output);
    assertThat(JdbcTestUtils.countRowsInTable(jdbcTemplate, "dog"))
        .isZero();
  }

  @Order(6)
  @TestCase("io.ngocnhan_tran1996.code.example.transaction.rabbitmq.testcase.TransactionTestArguments#testSendWithCloneArguments")
  void testSendEntityManagerWithoutTransaction(boolean input, int output) throws Exception {

    assertThatExceptionOfType(TransactionRequiredException.class)
        .isThrownBy(() -> producer.sendEntityManagerWithoutTransaction(input));
    var msgCount = rabbitAdmin.getQueueInfo(queue.getName())
        .getMessageCount();
    assertThat(msgCount)
        .isZero();
    assertThat(JdbcTestUtils.countRowsInTable(jdbcTemplate, "dog"))
        .isZero();
  }

}