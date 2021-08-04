package au.com.oculo.chat.repository

import au.com.oculo.chat.entity.Message
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface  MessageRepository : CrudRepository<Message, Long>