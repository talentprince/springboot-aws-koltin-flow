package org.weyoung.kotlinreactive.service

import org.springframework.stereotype.Service
import org.weyoung.kotlinreactive.receiver.SqsMessageHandler

@Service
class MessageService : SqsMessageHandler<UserMessage> {
    override fun handle(message: UserMessage) {
        println(message)
    }
}

data class UserMessage(val name: String)