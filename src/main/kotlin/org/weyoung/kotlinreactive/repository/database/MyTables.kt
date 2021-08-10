package org.weyoung.kotlinreactive.repository.database

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.weyoung.kotlinreactive.repository.database.entity.UserEntity
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable
import software.amazon.awssdk.enhanced.dynamodb.TableSchema

@Configuration
class MyTables {
    companion object {
        const val TABLE_NAME = "test-table"
    }

    @Bean
    fun userTable(dynamoDbEnhancedClient: DynamoDbEnhancedClient): DynamoDbTable<UserEntity> = dynamoDbEnhancedClient
        .table(TABLE_NAME, TableSchema.fromBean(UserEntity::class.java))
}