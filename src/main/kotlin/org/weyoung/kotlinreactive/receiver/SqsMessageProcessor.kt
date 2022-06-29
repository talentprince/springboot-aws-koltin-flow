package org.weyoung.kotlinreactive.receiver

import com.fasterxml.jackson.databind.ObjectMapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.retry
import software.amazon.awssdk.services.sqs.model.Message

class SqsMessageProcessor<T>(
    private val objectMapper: ObjectMapper,
    private val receiver: MessageReceiver<T>,
    private val clazz: Class<T>
) {
    fun process(message: Message) = objectMapper.readValue(message.body(), clazz).run(receiver::onReceive)

}

fun <T> Flow<Message>.process(processor: SqsMessageProcessor<T>): Flow<Message> = retry()
    .catch { println(it) }
    .onEach { processor.process(it) }