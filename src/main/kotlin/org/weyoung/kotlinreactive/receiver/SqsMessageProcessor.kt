package org.weyoung.kotlinreactive.receiver

import com.fasterxml.jackson.databind.ObjectMapper
import software.amazon.awssdk.services.sqs.model.Message

class SqsMessageProcessor<T>(
    private val objectMapper: ObjectMapper,
    private val receiver: MessageReceiver<T>,
    private val clazz: Class<T>
) {
    fun process(message: Message) = objectMapper.readValue(message.body(), clazz).run{
        receiver.onReceive(this)
    }

}
