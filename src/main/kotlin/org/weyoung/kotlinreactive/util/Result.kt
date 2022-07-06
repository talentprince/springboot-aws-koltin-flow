package org.weyoung.kotlinreactive.util

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.singleOrNull
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.bodyValueAndAwait
import org.springframework.web.reactive.function.server.buildAndAwait

suspend fun <T> Flow<Result<T>>.serverResponse() = coroutineScope {
    singleOrNull()?.let {
        if (it.isSuccess) ServerResponse.ok().bodyValueAndAwait(it.getOrNull()!!)
        else ServerResponse.badRequest().bodyValueAndAwait(Failure(it.exceptionOrNull()?.message))
    } ?: ServerResponse.notFound().buildAndAwait()
}

data class Failure(val reason: String?)