package org.weyoung.kotlinreactive.util

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.singleOrNull
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.bodyValueAndAwait
import org.springframework.web.reactive.function.server.buildAndAwait

class Response<out T> private constructor(private val value: Any) {
    val isSuccess = value !is Failure
    val isFailure = value is Failure

    companion object {
        suspend fun <T> handle(flow: Flow<Response<T>>) = coroutineScope {
            flow.singleOrNull()?.let {
                if (it.isSuccess) ServerResponse.ok().bodyValueAndAwait(it.get())
                else ServerResponse.badRequest().bodyValueAndAwait(it.get())
            } ?: ServerResponse.notFound().buildAndAwait()
        }

        fun <T> success(value: T): Response<T> = Response(value!!)
        fun <T> failure(throwable: Throwable): Response<T> = Response(Failure(throwable))
    }

    fun get() = value

    class Failure(exception: Throwable) {
        val reason: String? = exception.message
    }
}


