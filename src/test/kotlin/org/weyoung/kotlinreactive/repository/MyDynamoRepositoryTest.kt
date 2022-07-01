package org.weyoung.kotlinreactive.repository

import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.context.SpringBootTest
import org.weyoung.kotlinreactive.repository.database.entity.UserEntity
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient
import software.amazon.awssdk.enhanced.dynamodb.TableSchema
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.dynamodb.DynamoDbClient
import java.net.URI
import java.time.OffsetDateTime

@SpringBootTest
internal class MyDynamoRepositoryTest {
    @Autowired
    private lateinit var repository: MyDynamoRepository

    @Value("\${application.aws.endpoint}")
    private lateinit var endpoint: String

    @BeforeEach
    internal fun setUp() {
        DynamoDbEnhancedClient.builder()
            .dynamoDbClient(
                DynamoDbClient.builder()
                    .region(Region.US_EAST_1)
                    .endpointOverride(URI.create(endpoint))
                    .build()
            ).build().table("test-table", TableSchema.fromBean(UserEntity::class.java))
            .putItem(UserEntity(id = "1", sort = "USER", "Lala", OffsetDateTime.now()))
    }

    @Test
    internal fun `test query by id`() = runBlocking {
        repository.query("1", "USER").collect {
            assertEquals("Lala", it.name)
        }
    }
}