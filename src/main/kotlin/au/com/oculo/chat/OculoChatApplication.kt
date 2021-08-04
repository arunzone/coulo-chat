package au.com.oculo.chat

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class OculoChatApplication

fun main(args: Array<String>) {
    runApplication<OculoChatApplication>(*args)
}
