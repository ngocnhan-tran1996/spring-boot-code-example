package io.ngocnhan_tran1996.code.example.https.filter;

import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import java.util.function.Consumer;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ClientResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class WebClientFilter {

    private static final String BODY_VALUE_FIELD = "arg$1";

    public static Mono<WebClientRequest> filterRequest(ClientRequest request) {

        var clonedRequest = ClientRequest.from(request)
            .build();
        var webClientRequet = new WebClientRequest();
        webClientRequet.setMethod(clonedRequest.method());
        webClientRequet.setUrl(clonedRequest.url());
        webClientRequet.setHeaders(clonedRequest.headers());
        webClientRequet.setCookies(clonedRequest.cookies());

        try {

            var bodyInserter = clonedRequest.body();
            Field inserterField = bodyInserter.getClass().getDeclaredField(BODY_VALUE_FIELD);
            ReflectionUtils.makeAccessible(inserterField);

            webClientRequet.setBody(ReflectionUtils.getField(inserterField, bodyInserter));
        } catch (Exception e) {

            // do nothing
        }
        return Mono.just(webClientRequet);
    }

    public static Mono<ClientResponse> filterResponse(
        Tuple2<Long, ClientResponse> tuple,
        Consumer<WebClientResponse> onNext) {

        var response = tuple.getT2();
        var webClientResponse = new WebClientResponse();
        webClientResponse.setExecutionTimeMilis(tuple.getT1());
        webClientResponse.setStatusCode(response.statusCode());
        webClientResponse.setHeaders(response.headers().asHttpHeaders());
        webClientResponse.setCookies(response.cookies());
        return response.bodyToMono(DataBuffer.class)
            .map(body -> {

                webClientResponse.setBody(body.toString(StandardCharsets.UTF_8));
                return response.mutate()
                    .body(Flux.just(body))
                    .build();
            })
            .defaultIfEmpty(response)
            .doOnNext(clientResponse -> Optional.ofNullable(onNext)
                .ifPresent(consumer -> consumer.accept(webClientResponse)));
    }

}