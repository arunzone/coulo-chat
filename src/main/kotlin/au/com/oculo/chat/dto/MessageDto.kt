package au.com.oculo.chat.dto

import java.util.*


data class MessageDto(
    val id: Long? = null,
    val content: String,
    val recipients: List<UUID>,
    val sender: UUID
)
