package org.weyoung.kotlinreactive.receiver

import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.timeout
import org.mockito.kotlin.verify
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.weyoung.kotlinreactive.service.UserMessageReceiver
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.sqs.SqsClient
import software.amazon.awssdk.services.sqs.model.PurgeQueueRequest
import software.amazon.awssdk.services.sqs.model.SendMessageBatchRequest
import software.amazon.awssdk.services.sqs.model.SendMessageBatchRequestEntry
import java.net.URI

@SpringBootTest(properties = ["sqs.receiver.enabled=true"])
internal class SqsReceiverTest {
    @Value("\${application.aws.endpoint}")
    private lateinit var endpoint: String

    @Value("\${application.sqs.endpoint}")
    private lateinit var queueUrl: String

    @MockBean
    private lateinit var receiver: UserMessageReceiver

    @BeforeEach
    internal fun setUp() {
        SqsClient.builder().region(Region.US_EAST_1).endpointOverride(URI.create(endpoint)).build().run {
            purgeQueue(PurgeQueueRequest.builder().queueUrl(queueUrl).build())
            sendMessageBatch(
                SendMessageBatchRequest.builder()
                    .queueUrl(queueUrl)
                    .entries(
                        SendMessageBatchRequestEntry.builder()
                            .id("1")
                            .messageBody("""{"name":"James1"}""").build(),
                        SendMessageBatchRequestEntry.builder()
                            .id("2")
                            .messageBody("""{"name":"James2"}""").build(),
                        SendMessageBatchRequestEntry.builder()
                            .id("3")
                            .messageBody("""{"name":"James3"}""").build(),
                        SendMessageBatchRequestEntry.builder()
                            .id("4")
                            .messageBody("""{"name":"James4"}""").build(),
                        SendMessageBatchRequestEntry.builder()
                            .id("5")
                            .messageBody("""{"name":"James5"}""").build()
                    ).build()
            )
        }
    }

    @Test
    internal fun `test sqs receiver`() = runBlocking{
        verify(receiver, timeout(3000).times(5)).onReceive(any())
    }
}