package org.weyoung.kotlinreactive.repository.database.entity

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.*
import java.time.OffsetDateTime

@DynamoDbBean
data class UserEntity(
    @get:DynamoDbPartitionKey
    @get:DynamoDbAttribute("Id")
    var id:String,
    @get:DynamoDbSortKey
    @get:DynamoDbAttribute("Sort")
    var sort:String,
    @get:DynamoDbSecondaryPartitionKey(indexNames = ["test-name-index"])
    @get:DynamoDbAttribute("Name")
    var name:String,
    @get:DynamoDbAttribute("Timestamp")
    @get:DynamoDbConvertedBy(TimeConverter::class)
    var timestamp: OffsetDateTime
)