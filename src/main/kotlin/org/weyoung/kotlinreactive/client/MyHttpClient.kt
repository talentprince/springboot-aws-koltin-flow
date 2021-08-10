package org.weyoung.kotlinreactive.client

import kotlinx.coroutines.flow.Flow
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.*

@Component
class MyHttpClient(private val webClient: WebClient) {
    fun get(id: String): Flow<MyHttpResponse> = webClient.get()
        .uri("/user/$id")
        .exchangeToFlow(::responseToFlow)

    fun post(request: MyHttpRequest): Flow<MyHttpResponse> = webClient.post()
        .uri("/users")
        .body(request, MyHttpRequest::class.java).exchangeToFlow(::responseToFlow)

    private fun responseToFlow(it: ClientResponse): Flow<MyHttpResponse> {
        if (it.statusCode().is4xxClientError or it.statusCode().is5xxServerError)
            throw Exception("Error")
        return it.bodyToFlow()
    }
}

data class MyHttpResponse(val name: String)
class MyHttpRequest

