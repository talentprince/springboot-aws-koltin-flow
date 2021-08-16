package org.weyoung.kotlinreactive.config

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.weyoung.kotlinreactive.receiver.*
import org.weyoung.kotlinreactive.service.MessageService
import org.weyoung.kotlinreactive.service.UserMessage
import reactor.core.Disposable
import software.amazon.awssdk.services.sqs.SqsClient

@Configuration
class SqsReceiverConfig(@Value("\${application.sqs.endpoint}") private val queueUrl: String) {
    @Bean
    fun sqsReceiver(sqsClient: SqsClient) = SqsReceiver(sqsClient, 5)

    @Bean
    fun sqsCollector(sqsClient: SqsClient) = SqsMessageCollector(sqsClient, queueUrl)

    @Bean(destroyMethod = "dispose")
    fun userMessageProcessor(
        objectMapper: ObjectMapper,
        messageService: MessageService,
        sqsReceiver: SqsReceiver,
        sqsMessageCollector: SqsMessageCollector
    ): Disposable {
        val processor = SqsMessageProcessor(objectMapper, messageService, UserMessage::class.java)
        return sqsReceiver.receive(queueUrl).process(processor).collect(sqsMessageCollector)
    }
}