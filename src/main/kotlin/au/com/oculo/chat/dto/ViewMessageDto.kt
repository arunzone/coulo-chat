package au.com.oculo.chat.dto

import java.util.*


data class ViewMessageDto(
    val id: Long,
    val content: String,
    val sender: UUID
)
