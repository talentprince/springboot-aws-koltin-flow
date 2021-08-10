package org.weyoung.kotlinreactive.service

import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.stub
import org.weyoung.kotlinreactive.client.MyHttpClient
import org.weyoung.kotlinreactive.repository.MyDynamoRepository
import org.weyoung.kotlinreactive.repository.database.entity.UserEntity
import java.time.OffsetDateTime

@ExtendWith(MockitoExtension::class)
internal class MyServiceTest {
    @Mock
    lateinit var httpClient: MyHttpClient

    @Mock
    lateinit var dynamoRepository: MyDynamoRepository

    @InjectMocks
    lateinit var service: MyService

    private val timestamp = OffsetDateTime.parse("2021-08-10T06:00Z")

    @Test
    internal fun `given exist user id return user`() = runBlocking {
        dynamoRepository.stub { on { query("1", "USER") } doReturn
                    flowOf(UserEntity("1", "USER", "James", timestamp)) }

        service.get("1").collect { assertEquals(User("James", timestamp), it) }
    }
}