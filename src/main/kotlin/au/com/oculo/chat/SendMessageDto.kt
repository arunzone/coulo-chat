package au.com.oculo.chat

import java.util.*


data class SendMessageDto(
    val content: String,
    val recipients: List<UUID>,
    val sender: UUID
)
