package com.praveenukkoji.api_gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;

import java.time.LocalDateTime;

@SpringBootApplication
public class ApiGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(ApiGatewayApplication.class, args);
    }

    @Bean
    public RouteLocator RouteConfig(RouteLocatorBuilder routeLocatorBuilder) {
        return routeLocatorBuilder.routes()
                .route(p -> p
                        .path("/user-service/api/v1/users/**")
                        .filters(f ->
                                f.rewritePath("/user-service/(?<segment>.*)", "/${segment}")
                                        .addResponseHeader("X-Response-Time", LocalDateTime.now().toString()))
                        .uri("lb://USER-SERVICE")
                )
                .route(p -> p
                        .path("/product-service/api/v1/products/**")
                        .filters(f ->
                                f.rewritePath("/product-service/(?<segment>.*)", "/${segment}")
                                        .addResponseHeader("X-Response-Time", LocalDateTime.now().toString()))
                        .uri("lb://PRODUCT-SERVICE")
                )
                .route(p -> p
                        .path("/order-service/api/v1/orders/**")
                        .filters(f ->
                                f.rewritePath("/order-service/(?<segment>.*)", "/${segment}")
                                        .addResponseHeader("X-Response-Time", LocalDateTime.now().toString()))
                        .uri("lb://ORDER-SERVICE")
                )
                .route(p -> p
                        .path("/notification-service/**")
                        .filters(f ->
                                f.rewritePath("/notification-service/(?<segment>.*)", "/${segment}")
                                        .addResponseHeader("X-Response-Time", LocalDateTime.now().toString()))
                        .uri("lb://NOTIFICATION-SERVICE")
                )
                .route(p -> p
                        .path("/config-server/**")
                        .filters(f ->
                                f.rewritePath("/config-server/(?<segment>.*)", "/${segment}")
                                        .addResponseHeader("X-Response-Time", LocalDateTime.now().toString()))
                        .uri("lb://CONFIG-SERVER")
                )
                .build();
    }
}
