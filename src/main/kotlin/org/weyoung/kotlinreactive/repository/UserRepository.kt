package org.weyoung.kotlinreactive.repository

import org.weyoung.kotlinreactive.repository.database.DatabaseRepository

interface UserRepository<T> : DatabaseRepository<T> {
    fun findUserById()
}