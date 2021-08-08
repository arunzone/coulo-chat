package au.com.oculo.chat.service

import au.com.oculo.chat.dto.MessageDto
import au.com.oculo.chat.dto.ViewMessageDto
import au.com.oculo.chat.entity.Message
import au.com.oculo.chat.entity.MessageRecipient
import au.com.oculo.chat.entity.User
import au.com.oculo.chat.repository.MessageRepository
import au.com.oculo.chat.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*
import javax.transaction.Transactional

@Service
@Transactional
class MessageService(
    @Autowired private val messageRepository: MessageRepository,
    @Autowired private val userRepository: UserRepository,
    @Autowired private val emailService: EmailService
) {
    fun addMessage(messageDto: MessageDto): MessageDto {
        val sender = userRepository.findById(messageDto.sender).get()
        val recipients = messageDto.recipients.map { userRepository.findById(it).get() }

        val message = buildMessage(sender, recipients, messageDto.content)
        val savedMessage = messageRepository.save(message)
        senEmailFor(recipients, messageDto.content)

        return messagFrom(savedMessage)
    }

    private fun senEmailFor(recipients: List<User>, content: String) {
        recipients.forEach { recipient -> emailService.send(recipient.email, content) }
    }

    private fun messagFrom(savedMessage: Message) = MessageDto(
        id = savedMessage.id,
        content = savedMessage.content,
        sender = savedMessage.sender.id!!,
        recipients = savedMessage.recipients.map { it.recipient.id!! }
    )

    private fun buildMessage(sender: User, recipients: List<User>, content: String): Message {
        val message = Message(
            content = content,
            sender = sender
        )
        recipients.forEach {
            message.recipients.add(MessageRecipient(message = message, recipient = it))
        }

        return message
    }

    fun readMessages(sender: UUID, recipient: UUID): List<ViewMessageDto> {
        val messages = messageRepository.findAllBySender_IdAndRecipients_RecipientId(senderId = sender, recipientId = recipient)
        return if (messages.isNullOrEmpty()) emptyList() else toViewable(messages)
    }

    private fun toViewable(messages: List<Message>) =
        messages.map { ViewMessageDto(it.id!!, it.content, it.sender.id!!) }
}