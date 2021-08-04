package au.com.oculo.chat

import au.com.oculo.chat.dto.MessageDto
import io.kotest.assertions.assertSoftly
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.http.HttpEntity
import org.springframework.http.ResponseEntity
import java.net.URI
import java.util.*

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class OculoChatApplicationTests(@Autowired val restTemplate: TestRestTemplate) {
    @LocalServerPort
    var randomServerPort = 0

    @Test
    fun `should send new message`() {
        val baseUrl = "http://localhost:$randomServerPort/api/messages"
        val sender = UUID.fromString("8d1208fc-f401-496c-9cb8-483fef121234")
        val recipient = UUID.fromString("e6b920b7-4ac4-4b62-aea7-36f75e3ad610")
        val message = MessageDto(content = "Hi", sender = sender, recipients = listOf(recipient))
        val request: HttpEntity<MessageDto> = HttpEntity<MessageDto>(message)
        val result: ResponseEntity<MessageDto> = restTemplate.postForEntity(
            URI(baseUrl),
            request,
            MessageDto::class.java
        )

        val body = result.body
        assertSoftly {
            body!!.content shouldBe "Hi"
            body.sender shouldBe sender
            body.recipients.shouldContainExactly(recipient)
        }
    }

}
