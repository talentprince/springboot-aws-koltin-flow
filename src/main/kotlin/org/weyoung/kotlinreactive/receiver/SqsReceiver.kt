package org.weyoung.kotlinreactive.receiver

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import software.amazon.awssdk.services.sqs.SqsClient
import software.amazon.awssdk.services.sqs.model.Message
import software.amazon.awssdk.services.sqs.model.ReceiveMessageRequest

class SqsReceiver(
    private val sqsClient: SqsClient,
    private val parallelPollingCount: Int
) {
    fun receive(queueUrl: String): Flow<Message> = channelFlow {
        val request = ReceiveMessageRequest.builder()
            .maxNumberOfMessages(5)
            .waitTimeSeconds(10)
            .visibilityTimeout(30)
            .queueUrl(queueUrl)
            .build()
        repeat(parallelPollingCount) {
            launch(Dispatchers.IO) {
                while (isActive) {
                    sqsClient.receiveMessage(request).messages().map {
                        println("Polling message from [${Thread.currentThread().name}]")
                        send(it)
                    }
                }
            }
        }
    }
}
