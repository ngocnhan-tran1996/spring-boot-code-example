package io.ngocnhan_tran1996.code.example.gateway;

import org.hamcrest.core.Is;
import org.hamcrest.number.OrderingComparison;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class GatewayApplicationTests {

    @Autowired
    WebTestClient webClient;

    @Test
    void testGateway() {

        this.webClient
            .get()
            .uri("/catfact/fact")
            .exchange()
            .expectStatus().isOk()
            .expectBody()
            .jsonPath("$.length").value(OrderingComparison.greaterThan(1))
            .jsonPath("$.fact").exists();

        this.webClient
            .post()
            .uri("/api/v1/create")
            .bodyValue("{\"name\":\"test\",\"salary\":\"123\",\"age\":\"23\"}")
            .exchange()
            .expectStatus().isOk()
            .expectBody()
            .jsonPath("$.status").value(Is.is("success"))
            .jsonPath("$.message").value(Is.is("Successfully! Record has been added."))
            .jsonPath("$.data").exists();
    }

}