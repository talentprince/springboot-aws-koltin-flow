package org.weyoung.kotlinreactive.receiver

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.reactor.mono
import software.amazon.awssdk.services.sqs.SqsClient
import software.amazon.awssdk.services.sqs.model.DeleteMessageRequest
import software.amazon.awssdk.services.sqs.model.Message

class SqsMessageCollector(
    private val sqsClient: SqsClient,
    private val queueUrl: String
) {
    fun commit(value: Message) {
        DeleteMessageRequest.builder()
            .queueUrl(queueUrl)
            .receiptHandle(value.receiptHandle())
            .build()
            .run {
                println("Deleting message [${Thread.currentThread().name}]")
                sqsClient.deleteMessage(this)
            }
    }
}

fun Flow<Message>.collect(collector: SqsMessageCollector) =
    mono {
        collect {
            collector.commit(it)
        }
    }.subscribe()
