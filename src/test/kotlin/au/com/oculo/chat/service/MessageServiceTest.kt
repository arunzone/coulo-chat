package au.com.oculo.chat.service

import au.com.oculo.chat.dto.MessageDto
import au.com.oculo.chat.entity.Message
import au.com.oculo.chat.entity.MessageRecipient
import au.com.oculo.chat.entity.User
import au.com.oculo.chat.repository.MessageRepository
import au.com.oculo.chat.repository.UserRepository
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.core.test.TestCase
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.shouldBe
import io.mockk.MockKAnnotations
import io.mockk.Runs
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.just
import io.mockk.verify
import java.util.*

internal class MessageServiceTest : ShouldSpec() {
    @MockK
    lateinit var userRepository: UserRepository

    @MockK
    lateinit var messageRepository: MessageRepository

    @MockK
    lateinit var emailService: EmailService

    private lateinit var messageService: MessageService

    override fun beforeTest(testCase: TestCase) {
        super.beforeTest(testCase)
        MockKAnnotations.init(this)
        messageService = MessageService(messageRepository, userRepository, emailService)
    }

    init {
        context("Send new message") {
            val userId = UUID.randomUUID()
            val recipientId = UUID.randomUUID()
            val sender = User(userId, "email.com")
            val recipient = User(recipientId, "mark@oculo.com.au")
            every { userRepository.findById(userId) } answers { Optional.of(sender) }
            every { emailService.send(any(), any()) } just Runs
            every { userRepository.findById(recipientId) } answers { Optional.of(recipient) }
            every { messageRepository.save(any()) } answers {
                val savedMessage = Message(id = 1, content = "Hi", sender = sender)
                savedMessage.recipients.add(MessageRecipient(message = savedMessage, recipient = recipient))
                savedMessage
            }

            val messageDto = MessageDto(content = "Hi", sender = userId, recipients = listOf(recipientId))
            val message = messageService.addMessage(messageDto)

            should("have message Hi") {
                verify { messageRepository.save(withArg { it.content shouldBe "Hi" }) }
            }
            should("have sender") {
                verify { messageRepository.save(withArg { it.sender shouldBe sender }) }
            }
            should("have recipients") {
                verify {
                    messageRepository.save(withArg {
                        val recipients = it.recipients.map { recipient -> recipient.recipient }
                        recipients.shouldContainExactly(recipient)
                    })
                }
            }
            should("respond with message details") {
                val expectedMessage = MessageDto(id = 1, content = "Hi", sender = userId, recipients = listOf(recipientId))
                message shouldBe expectedMessage
            }
            should("send email") {
                verify { emailService.send("mark@oculo.com.au", "Hi") }
            }
        }


    }
}
