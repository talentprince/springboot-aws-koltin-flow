package org.weyoung.kotlinreactive.config

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.client.WebClient
import org.weyoung.kotlinreactive.receiver.SqsReceiver
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.dynamodb.DynamoDbClient
import software.amazon.awssdk.services.sqs.SqsAsyncClient
import java.net.URI

@Configuration
class MyConfig(@Value("\${application.dynamo.endpoint}") private val endpoint: String) {
    @Bean
    fun dynamoDbClient(): DynamoDbClient = DynamoDbClient.builder()
        .region(Region.AP_EAST_1)
        .endpointOverride(URI.create(endpoint))
        .build()

    @Bean
    fun dynamoDbEnhancedClient(dynamoDbClient: DynamoDbClient): DynamoDbEnhancedClient =
        DynamoDbEnhancedClient.builder()
            .dynamoDbClient(dynamoDbClient)
            .build()

    @Bean
    fun webClient() = WebClient.builder()
        .baseUrl("http://localhost:3000")
        .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
        .build()

    @Bean
    fun sqsAsyncClient(): SqsAsyncClient = SqsAsyncClient.builder()
        .region(Region.AP_EAST_1)
        .endpointOverride(URI.create(endpoint))
        .build()

    @Bean
    fun sqsReceiver(sqsAsyncClient: SqsAsyncClient, objectMapper: ObjectMapper) =
        SqsReceiver(sqsAsyncClient, objectMapper)
}