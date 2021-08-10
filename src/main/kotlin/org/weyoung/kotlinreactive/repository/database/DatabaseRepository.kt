package org.weyoung.kotlinreactive.repository.database

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable
import software.amazon.awssdk.enhanced.dynamodb.Key
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional

interface DatabaseRepository<T> {
    val table: DynamoDbTable<T>

    fun save(item: T): Flow<T> = flow {
        table.putItem(item)
        emit(item)
    }

    fun saveAll(items: List<T>): Flow<List<T>> = flow {
        emit(items.map {
            table.putItem(it)
            it
        })
    }

    fun findAll(): Flow<List<T>> = flow { table.scan().mapNotNull { it.items() }.flatten().let { emit(it) } }

    fun deleteAll(): Flow<List<T>> = findAll().map { it.map { item -> table.deleteItem(item) } }

    fun query(key: String, sort: String): Flow<T> = flow {
        table.getItem(Key.builder().partitionValue(key).sortValue(sort).build())
            ?.let { emit(it) } ?: emptyFlow<T>()
    }

    fun query(key: String): Flow<List<T>> = flow {
        table.query(QueryConditional.keyEqualTo(Key.builder().partitionValue(key).build()))
            .mapNotNull { it.items() }.flatten().let { emit(it) }
    }

    fun queryByIndex(indexName: String, indexKey: String): Flow<List<T>> = flow {
        table.index(indexName).query(QueryConditional.keyEqualTo(Key.builder().partitionValue(indexKey).build()))
            .mapNotNull { it.items() }.flatten().let { emit(it) }
    }
}