package org.weyoung.kotlinreactive.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.weyoung.kotlinreactive.repository.database.entity.UserEntity
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable
import software.amazon.awssdk.enhanced.dynamodb.TableSchema

@Configuration
class MyTables {
    @Bean
    fun userTable(
        @Value("\${dynamo.table.name}")
        tableName: String,
        dynamoDbEnhancedClient: DynamoDbEnhancedClient): DynamoDbTable<UserEntity> = dynamoDbEnhancedClient
        .table(tableName, TableSchema.fromBean(UserEntity::class.java))
}