package org.weyoung.kotlinreactive.receiver

interface SqsMessageHandler<T> {
    fun handle(message: T)
}
