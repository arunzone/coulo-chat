package au.com.oculo.chat.service

import au.com.oculo.chat.SendMessageDto
import au.com.oculo.chat.entity.Message
import au.com.oculo.chat.entity.MessageRecipient
import au.com.oculo.chat.entity.User
import au.com.oculo.chat.repository.MessageRepository
import au.com.oculo.chat.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import javax.transaction.Transactional

@Service
@Transactional
class MessageService(
    @Autowired private val messageRepository: MessageRepository,
    @Autowired private val userRepository: UserRepository
) {
    fun addMessage(messageDto: SendMessageDto) {
        val sender = userRepository.findById(messageDto.sender).get()
        val recipients = messageDto.recipients.map { userRepository.findById(it).get() }

        val message = buildMessage(sender, recipients, messageDto.content)

        messageRepository.save(message)
    }

    private fun buildMessage(sender: User, recipients: List<User>, content: String): Message {
        val message = Message(
            content = content,
            sender = sender
        )
        recipients.forEach {
            val recipient = MessageRecipient(message = message, recipient = it)
            message.addRecipient(recipient)
        }
        return message
    }
}