package io.ngocnhan_tran1996.code.example.transaction.datasource.rabbitmq;

import io.ngocnhan_tran1996.code.example.transaction.domain.DogEntity;
import io.ngocnhan_tran1996.code.example.transaction.domain.DogRepo;
import io.ngocnhan_tran1996.code.example.utils.Strings;
import jakarta.persistence.EntityManager;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
class DataSourceRabbitMQConfigProducer {

    final CountDownLatch latch = new CountDownLatch(1);
    private final Queue queue;
    private final RabbitTemplate rabbitTemplate;
    private final DogRepo dogRepo;
    private final JdbcTemplate jdbcTemplate;
    private final EntityManager entityManager;

    @Transactional
    void send(boolean hasTransaction) {

        this.rabbitTemplate.setChannelTransacted(hasTransaction);

        var dog = new DogEntity(1, "Chiahaha");
        dogRepo.save(dog);
        this.rabbitTemplate.convertAndSend(queue.getName(), Strings.toSafeString(dog));

        try {
            latch.await(100, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {

            // do nothing
        }
        throw new RuntimeException("Failure save dog");
    }

    void sendWithoutTransaction(boolean hasTransaction) {

        this.send(hasTransaction);
    }

    @Transactional
    void sendJdbc(boolean hasTransaction) {

        this.rabbitTemplate.setChannelTransacted(hasTransaction);

        var dog = this.jdbcTemplate.update(
            "INSERT INTO DOG (id, species) VALUES (?, ?)",
            1, "Chihuahua");
        this.rabbitTemplate.convertAndSend(queue.getName(), Strings.toSafeString(dog));

        try {
            latch.await(100, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {

            // do nothing
        }
        throw new RuntimeException("Failure save dog");
    }

    void sendJdbcWithoutTransaction(boolean hasTransaction) {

        this.sendJdbc(hasTransaction);
    }

    @Transactional
    void sendEntityManager(boolean hasTransaction) {

        this.rabbitTemplate.setChannelTransacted(hasTransaction);

        var dog = this.entityManager.createNativeQuery(
                "INSERT INTO DOG (id, species) VALUES (?, ?)")
            .setParameter(1, 1)
            .setParameter(2, "Chihuahua")
            .executeUpdate();
        this.rabbitTemplate.convertAndSend(queue.getName(), Strings.toSafeString(dog));

        throw new RuntimeException("Failure save dog");
    }

    void sendEntityManagerWithoutTransaction(boolean hasTransaction) {

        this.sendEntityManager(hasTransaction);
    }

}