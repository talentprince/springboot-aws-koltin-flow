package org.weyoung.kotlinreactive.service

import org.springframework.stereotype.Service
import org.weyoung.kotlinreactive.receiver.MessageReceiver

@Service
class UserMessageReceiver : MessageReceiver<UserMessage> {
    override fun onReceive(message: UserMessage) {
        println(message)
    }
}

data class UserMessage(val name: String)