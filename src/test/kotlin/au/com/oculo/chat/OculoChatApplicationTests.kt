package au.com.oculo.chat

import au.com.oculo.chat.dto.MessageDto
import au.com.oculo.chat.dto.ViewMessageDto
import io.kotest.assertions.assertSoftly
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.longs.shouldBeGreaterThan
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.HttpEntity
import org.springframework.http.HttpMethod
import org.springframework.http.ResponseEntity
import org.springframework.test.context.ActiveProfiles
import org.springframework.util.MultiValueMapAdapter
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import java.net.URI
import java.util.*


@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles(value = ["test"])
class OculoChatApplicationTests(
    @Autowired private val restTemplate: TestRestTemplate,
) {
    @LocalServerPort
    var randomServerPort = 0

    companion object {
        @Container
        val container = PostgreSQLContainer<Nothing>("postgres:13").apply {
            withDatabaseName("postgres")
            withExposedPorts(5435)
        }

    }

    @Test
    fun `should send new message`() {
        val baseUrl = "http://localhost:$randomServerPort/api/messages"
        val sender = UUID.fromString("a6f748d6-905f-4040-9849-2fd1f96b7364")
        val recipient = UUID.fromString("f4d6a29e-bf44-47d1-b68b-3de97a80ec12")
        val message = MessageDto(content = "Hi", sender = sender, recipients = listOf(recipient))
        val request: HttpEntity<MessageDto> = HttpEntity<MessageDto>(message)
        val result: ResponseEntity<MessageDto> = restTemplate.postForEntity(
            URI(baseUrl),
            request,
            MessageDto::class.java
        )

        val body = result.body
        assertSoftly {
            result.statusCodeValue shouldBe 200
            body!!.content shouldBe "Hi"
            body.sender shouldBe sender
            body.recipients.shouldContainExactly(recipient)
            body.id!! shouldBeGreaterThan 0
        }
    }

    @Test
    fun `should receive messages`() {

        val sender = UUID.fromString("83cd7615-f247-4811-8265-8d89c5615be9")
        val recipient = UUID.fromString("0fd581e1-f1b7-4a18-b146-ceeac7adadc4")
        val baseUrl = "http://localhost:$randomServerPort/api/messages/senders/$sender/recipients/$recipient"
        val result: ResponseEntity<List<ViewMessageDto>> = restTemplate.exchange(
            URI(baseUrl),
            HttpMethod.GET,
            null,
            object : ParameterizedTypeReference<List<ViewMessageDto>>() {}
        )

        val body = result.body
        body!!.shouldContainExactly(
            ViewMessageDto(1, "Hello", sender),
            ViewMessageDto(2, "Wasup", sender)
        )
    }

    @Test
    fun `should receive messages in xml type`() {

        val sender = UUID.fromString("83cd7615-f247-4811-8265-8d89c5615be9")
        val recipient = UUID.fromString("0fd581e1-f1b7-4a18-b146-ceeac7adadc4")
        val baseUrl = "http://localhost:$randomServerPort/api/messages/senders/$sender/recipients/$recipient"
        val headers = MultiValueMapAdapter(
            mapOf(
                "Content-Type" to listOf("text/xml"),
                "accept" to listOf("text/xml"),
            )
        )
        val request: HttpEntity<MessageDto> = HttpEntity<MessageDto>(headers)
        val result: ResponseEntity<List<ViewMessageDto>> = restTemplate.exchange(
            URI(baseUrl),
            HttpMethod.GET,
            request,
            object : ParameterizedTypeReference<List<ViewMessageDto>>() {}
        )

        val body = result.body
        body!!.shouldContainExactly(
            ViewMessageDto(1, "Hello", sender),
            ViewMessageDto(2, "Wasup", sender)
        )
    }

}
