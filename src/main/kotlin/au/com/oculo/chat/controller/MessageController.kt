package au.com.oculo.chat.controller

import au.com.oculo.chat.dto.MessageDto
import au.com.oculo.chat.dto.ViewMessageDto
import au.com.oculo.chat.service.MessageService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController
class MessageController(@Autowired private val messageService: MessageService) {
    @PostMapping(
        "/api/messages",
        produces = [MediaType.APPLICATION_JSON_VALUE, MediaType.TEXT_XML_VALUE]
    )
    fun addMessage(@RequestBody message: MessageDto): MessageDto {
        return messageService.addMessage(message)
    }

    @GetMapping(
        "/api/messages/senders/{senderId}/recipients/{recipientId}",
        produces = [MediaType.APPLICATION_JSON_VALUE, MediaType.TEXT_XML_VALUE]
    )
    fun readMessage(@PathVariable senderId: UUID, @PathVariable recipientId: UUID): List<ViewMessageDto> {
        return messageService.readMessages(senderId, recipientId)
    }
}