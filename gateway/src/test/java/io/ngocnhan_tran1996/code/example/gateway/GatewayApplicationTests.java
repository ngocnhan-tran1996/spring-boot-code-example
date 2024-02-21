package io.ngocnhan_tran1996.code.example.gateway;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class GatewayApplicationTests {

    @Autowired
    private WebTestClient webClient;

    @Test
    void testGateway() {

        this.webClient
            .get()
            .uri("/fact")
            .exchange()
            .expectStatus().isOk()
            .expectBody()
            .jsonPath("$.fact").exists();
    }

}