package org.weyoung.kotlinreactive.repository.database.entity

import software.amazon.awssdk.enhanced.dynamodb.AttributeConverter
import software.amazon.awssdk.enhanced.dynamodb.AttributeValueType
import software.amazon.awssdk.enhanced.dynamodb.EnhancedType
import software.amazon.awssdk.services.dynamodb.model.AttributeValue
import java.time.OffsetDateTime

class TimeConverter : AttributeConverter<OffsetDateTime> {
    override fun transformFrom(input: OffsetDateTime): AttributeValue = AttributeValue.builder().s(input.toString()).build()

    override fun transformTo(input: AttributeValue): OffsetDateTime = input.let { OffsetDateTime.parse(input.s()) }

    override fun type(): EnhancedType<OffsetDateTime> = EnhancedType.of(OffsetDateTime::class.java)

    override fun attributeValueType() = AttributeValueType.S
}
