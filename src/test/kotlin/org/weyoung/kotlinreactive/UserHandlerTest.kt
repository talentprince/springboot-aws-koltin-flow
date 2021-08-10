package org.weyoung.kotlinreactive

import kotlinx.coroutines.flow.flowOf
import org.junit.jupiter.api.Test
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.stub
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.test.web.reactive.server.expectBody
import org.weyoung.kotlinreactive.service.MyService
import org.weyoung.kotlinreactive.service.User
import java.time.OffsetDateTime


@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT)
class UserHandlerTest {
    @Autowired
    lateinit var webClient: WebTestClient
    @MockBean
    lateinit var service: MyService

    @Test
    internal fun `get valid user`() {
        val timestamp = OffsetDateTime.parse("2021-08-10T06:00Z")
        service.stub { on { get("1") } doReturn flowOf(User("James", timestamp)) }

        webClient.get()
            .uri("/api/user/1")
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .is2xxSuccessful
            .expectBody<User>()
            .isEqualTo(User("James", timestamp))
    }
}