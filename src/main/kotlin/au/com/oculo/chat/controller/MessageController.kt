package au.com.oculo.chat.controller

import au.com.oculo.chat.dto.MessageDto
import au.com.oculo.chat.dto.ViewMessageDto
import au.com.oculo.chat.service.MessageService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController
@RequestMapping("/api/messages") // must start with "/api/" so that front-end app can talk to
class MessageController(@Autowired private val messageService: MessageService) {
    @PostMapping
    fun addMessage(@RequestBody message: MessageDto): MessageDto {
        return messageService.addMessage(message)
    }

    @GetMapping
    fun readMessage(@RequestParam sender: UUID, @RequestParam recipient: UUID): List<ViewMessageDto> {
        return messageService.readMessages(sender, recipient)
    }
}