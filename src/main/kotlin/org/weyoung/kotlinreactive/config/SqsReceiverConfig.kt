package org.weyoung.kotlinreactive.config

import com.fasterxml.jackson.databind.ObjectMapper
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onErrorResume
import kotlinx.coroutines.flow.onErrorReturn
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.weyoung.kotlinreactive.receiver.*
import org.weyoung.kotlinreactive.service.UserMessageReceiver
import org.weyoung.kotlinreactive.service.UserMessage
import reactor.core.Disposable
import software.amazon.awssdk.services.sqs.SqsClient
import software.amazon.eventstream.Message

@Configuration
@ConditionalOnProperty(prefix = "sqs", name = ["receiver.enabled"], matchIfMissing = false)
class SqsReceiverConfig(@Value("\${application.sqs.endpoint}") private val queueUrl: String) {
    @Bean
    fun sqsReceiver(sqsClient: SqsClient) = SqsReceiver(sqsClient, 5)

    @Bean
    fun sqsCollector(sqsClient: SqsClient) = SqsMessageCollector(sqsClient, queueUrl)

    @Bean(destroyMethod = "dispose")
    fun userMessageProcessor(
        objectMapper: ObjectMapper,
        userMessageReceiver: UserMessageReceiver,
        sqsReceiver: SqsReceiver,
        sqsMessageCollector: SqsMessageCollector
    ): Disposable {
        val processor = SqsMessageProcessor(objectMapper, userMessageReceiver, UserMessage::class.java)
        return sqsReceiver.receive(queueUrl)
            .onEach { processor.process(it) }
            .collect(sqsMessageCollector)
    }
}