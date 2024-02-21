package io.ngocnhan_tran1996.code.example.transaction.entitymanager;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import io.ngocnhan_tran1996.code.example.transaction.domain.DogEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TransactionRequiredException;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@ActiveProfiles("postgres")
@SpringBootTest
@TestMethodOrder(OrderAnnotation.class)
class EntityManagerTest {

    @Autowired
    EntityManager entityManager;

    @Test
    @Transactional
    void testInsertWithTransaction() {

        assertThat(
            this.entityManager.createQuery("FROM DogEntity", DogEntity.class).getResultList())
            .isEmpty();
        this.entityManager.createNativeQuery("INSERT INTO DOG (id, species) VALUES (?, ?)")
            .setParameter(1, 1)
            .setParameter(2, "Chihuahua")
            .executeUpdate();
        assertThat(
            this.entityManager.createQuery("FROM DogEntity", DogEntity.class).getResultList())
            .hasSize(1);
    }

    @Test
    void testInsertWithoutTransaction() {

        assertThat(
            this.entityManager.createQuery("FROM DogEntity", DogEntity.class).getResultList())
            .isEmpty();
        var query = this.entityManager.createNativeQuery(
                "INSERT INTO DOG (id, species) VALUES (?, ?)")
            .setParameter(1, 1)
            .setParameter(2, "Chihuahua");
        assertThatExceptionOfType(TransactionRequiredException.class)
            .isThrownBy(query::executeUpdate);
        assertThat(
            this.entityManager.createQuery("FROM DogEntity", DogEntity.class).getResultList())
            .isEmpty();
    }

}