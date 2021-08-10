package org.weyoung.kotlinreactive.receiver

import com.fasterxml.jackson.databind.ObjectMapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.launch
import software.amazon.awssdk.services.sqs.SqsAsyncClient
import software.amazon.awssdk.services.sqs.model.ReceiveMessageRequest

class SqsReceiver(
    @PublishedApi internal val sqsAsyncClient: SqsAsyncClient,
    @PublishedApi internal val objectMapper: ObjectMapper
) {
    inline fun <reified T> receive(queueName: String): Flow<T> = channelFlow {
        val request = ReceiveMessageRequest.builder()
            .maxNumberOfMessages(1)
            .queueUrl(queueName).build()
        repeat((1..5).count()) {
            launch(Dispatchers.IO) {
                while (true) {
                    sqsAsyncClient.receiveMessage(request).get().messages().map {
                        println("Polling message from [${Thread.currentThread().name}]")
                        send(objectMapper.readValue(it.body(), T::class.java))
                    }
                }
            }
        }
    }
}
