package org.weyoung.kotlinreactive.receiver

import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.context.SpringBootTest
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.sqs.SqsClient
import software.amazon.awssdk.services.sqs.model.SendMessageBatchRequest
import software.amazon.awssdk.services.sqs.model.SendMessageBatchRequestEntry
import java.net.URI

@SpringBootTest
internal class SqsReceiverTest {
    @Value("\${application.aws.endpoint}")
    private lateinit var endpoint: String

    @Value("\${application.sqs.endpoint}")
    private lateinit var queueUrl: String

    @BeforeEach
    internal fun setUp() {
        SqsClient.builder().region(Region.AP_EAST_1).endpointOverride(URI.create(endpoint)).build().run {
            sendMessageBatch(
                SendMessageBatchRequest.builder()
                    .queueUrl(queueUrl)
                    .entries(
                        SendMessageBatchRequestEntry.builder()
                            .id("1")
                            .delaySeconds(1)
                            .messageBody("""{"name":"James1"}""").build(),
                        SendMessageBatchRequestEntry.builder()
                            .id("2")
                            .delaySeconds(2)
                            .messageBody("""{"name":"James2"}""").build(),
                        SendMessageBatchRequestEntry.builder()
                            .id("3")
                            .delaySeconds(3)
                            .messageBody("""{"name":"James3"}""").build(),
                        SendMessageBatchRequestEntry.builder()
                            .id("4")
                            .delaySeconds(4)
                            .messageBody("""{"name":"James4"}""").build(),
                        SendMessageBatchRequestEntry.builder()
                            .id("5")
                            .delaySeconds(5)
                            .messageBody("""{"name":"James5"}""").build()
                    ).build()
            )
        }
    }

    @Test
    internal fun `test sqs receiver`() = runBlocking {
        delay(6000)
    }

    data class QueueMessage(val name: String)
}