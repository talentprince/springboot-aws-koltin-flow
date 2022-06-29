package org.weyoung.kotlinreactive.receiver

interface MessageReceiver<T> {
    fun onReceive(message: T)
}
