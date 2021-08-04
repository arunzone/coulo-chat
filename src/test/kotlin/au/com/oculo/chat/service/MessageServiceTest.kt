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
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import java.util.*

internal class MessageServiceTest : ShouldSpec() {
    @MockK
    lateinit var userRepository: UserRepository

    @MockK
    lateinit var messageRepository: MessageRepository

    private lateinit var messageService: MessageService

    override fun beforeTest(testCase: TestCase) {
        super.beforeTest(testCase)
        MockKAnnotations.init(this)
        messageService = MessageService(messageRepository, userRepository)
    }

    init {
        context("Send new message") {
            val userId = UUID.randomUUID()
            val recipientId = UUID.randomUUID()
            val sender = User(userId, "email.com")
            val recipient = User(recipientId, "email.com")
            every { userRepository.findById(userId) } answers { Optional.of(sender) }
            every { userRepository.findById(recipientId) } answers { Optional.of(recipient) }
            every { messageRepository.save(any()) } answers {
                val savedMessage = Message(content = "Hi", sender = sender)
                savedMessage.addRecipient(MessageRecipient(message = savedMessage, recipient = recipient))
                savedMessage
            }

            val messageDto = MessageDto(content = "Hi", sender = userId, recipients = listOf(recipientId))
            val message = messageService.addMessage(messageDto)

            should("have message Hi") {
                verify { messageRepository.save(Message(content = "Hi", sender = sender)) }
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
                val expectedMessage = MessageDto(id = -1, content = "Hi", sender = userId, recipients = listOf(recipientId))
                message shouldBe expectedMessage
            }
        }


    }
}
