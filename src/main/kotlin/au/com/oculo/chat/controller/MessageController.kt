package au.com.oculo.chat.controller

import au.com.oculo.chat.dto.MessageDto
import au.com.oculo.chat.service.MessageService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/messages") // must start with "/api/" so that front-end app can talk to
class MessageController(@Autowired private val messageService: MessageService) {
    @PostMapping()
    fun addMessage(@RequestBody message: MessageDto): MessageDto {
        return messageService.addMessage(message)
    }
}