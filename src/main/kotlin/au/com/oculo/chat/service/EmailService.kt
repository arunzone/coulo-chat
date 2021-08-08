package au.com.oculo.chat.service

import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service

@Service
class EmailService {
    @Async
    fun send(email: String, body: String) {
        // functionality to implement sendingg email
    }
}