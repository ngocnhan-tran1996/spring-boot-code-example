package io.ngocnhan_tran1996.code.example.https;

import static org.assertj.core.api.Assertions.assertThat;

import io.ngocnhan_tran1996.code.example.https.HomeController.Person;
import io.ngocnhan_tran1996.code.example.https.filter.WebClientFilter;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.web.reactive.function.client.WebClient;

@Slf4j
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT, classes = HomeController.class)
class WebClientTest {

    @LocalServerPort
    int port;

    WebClient webClient;

    @BeforeEach
    void init() {

        webClient = WebClient.builder()
            .filter((request, next) -> WebClientFilter.filterRequest(request)
                .doOnNext(webRequeset -> log.info("Request {}:", webRequeset.getBody()))
                .then(next.exchange(request)))
            .filter((request, next) -> next.exchange(request)
                .elapsed()
                .flatMap(tuple -> WebClientFilter.filterResponse(
                    tuple,
                    response -> log.info("Response: {}", response.getBody()))))
            .build();
    }

    @Test
    void testPost() {

        var person = new Person("Nhan", 20);
        var output = webClient.post()
            .uri("http://localhost:" + port + "/")
            .bodyValue(person)
            .retrieve()
            .bodyToMono(Person.class)
            .block();

        assertThat(output).isEqualTo(person);
    }
}