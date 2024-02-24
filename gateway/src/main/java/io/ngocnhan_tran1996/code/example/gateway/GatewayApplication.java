package io.ngocnhan_tran1996.code.example.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class GatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(GatewayApplication.class, args);
    }

    @Bean
    public RouteLocator routes(RouteLocatorBuilder builder) {
        return builder.routes()
            .route(
                predicateSpec -> predicateSpec
                    .path("/catfact/**")
                    .filters(gatewayFilterSpec -> gatewayFilterSpec
                        .rewritePath("/catfact/(?<segment>.*)", "/${segment}")
                    )
                    .uri("https://catfact.ninja")
            )
            .route(
                predicateSpec -> predicateSpec
                    .path("/api/v1/**")
                    .uri("https://dummy.restapiexample.com")
            )
            .build();
    }

}