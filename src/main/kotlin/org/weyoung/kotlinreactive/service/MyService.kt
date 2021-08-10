package org.weyoung.kotlinreactive.service

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.map
import org.springframework.stereotype.Service
import org.weyoung.kotlinreactive.client.MyHttpClient
import org.weyoung.kotlinreactive.repository.MyDynamoRepository
import org.weyoung.kotlinreactive.repository.database.entity.UserEntity
import java.time.OffsetDateTime

@Service
class MyService(val httpClient: MyHttpClient, val dynamoRepository: MyDynamoRepository) {
    fun get(id: String): Flow<User> = dynamoRepository.query(id, "USER").map {
        User(it.name, it.timestamp)
    }

    fun queryByName(name: String): Flow<List<User>> = dynamoRepository.queryByIndex("test-name-index", name)
        .map { it.map(UserEntity::toUser) }

    fun queryById(id: String): Flow<List<User>> = dynamoRepository.query(id).map { it.map(UserEntity::toUser) }

    fun create(id: String): Flow<User> = httpClient.get(id).flatMapConcat {
        dynamoRepository.save(UserEntity(id, "USER", it.name, OffsetDateTime.now())).map(UserEntity::toUser)
    }

}

fun UserEntity.toUser() = User(name, timestamp)
data class User(val name: String, val timestamp: OffsetDateTime)

