package org.weyoung.kotlinreactive.repository

import org.springframework.stereotype.Repository
import org.weyoung.kotlinreactive.repository.database.DatabaseRepository
import org.weyoung.kotlinreactive.repository.database.entity.UserEntity
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable

@Repository
class MyDynamoRepository(userTable: DynamoDbTable<UserEntity>) : DatabaseRepository<UserEntity> {
    override val table = userTable
}