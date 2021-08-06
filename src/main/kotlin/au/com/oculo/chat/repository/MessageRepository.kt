package au.com.oculo.chat.repository

import au.com.oculo.chat.entity.Message
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface MessageRepository : CrudRepository<Message, Long> {
    fun findAllBySender_IdAndRecipients_RecipientId(senderId: UUID, recipientId: UUID): List<Message>?
}