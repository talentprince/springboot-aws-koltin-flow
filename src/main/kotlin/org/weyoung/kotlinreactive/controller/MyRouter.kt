package org.weyoung.kotlinreactive.controller

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.web.reactive.function.server.coRouter
import org.weyoung.kotlinreactive.controller.handler.UserHandler

@Configuration
class MyRouter(val handler: UserHandler) {
    @Bean
    fun mainRouter() = coRouter {
        accept(APPLICATION_JSON).nest {
            GET("/api/user/{id}", handler::get)
            POST("/api/user", handler::create)
        }
    }
}