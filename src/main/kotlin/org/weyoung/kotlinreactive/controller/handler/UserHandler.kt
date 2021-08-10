package org.weyoung.kotlinreactive.controller.handler

import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.map
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.bodyToFlow
import org.weyoung.kotlinreactive.service.MyService
import org.weyoung.kotlinreactive.util.Response

@Component
class UserHandler(private val service: MyService) {
    suspend fun get(request: ServerRequest) = request.pathVariable("id").let {
        service.get(it)
            .map { user -> Response.success(user) }
            .let { result -> Response.handle(result) }
    }

    suspend fun create(request: ServerRequest) = request
        .bodyToFlow<UserCreateRequest>()
        .flatMapConcat { service.create(it.id) }
        .map { Response.success(UserCreateSuccess("Success")) }
        .catch { emit(Response.failure(it)) }
        .let { Response.handle(it) }
}

data class UserCreateRequest(val id: String)
data class UserCreateSuccess(val message: String)