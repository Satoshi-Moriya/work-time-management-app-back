package com.example.worktimemanagement.controller

import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.messaging.handler.annotation.SendTo
import org.springframework.stereotype.Controller

@Controller
class ChatController {

    @MessageMapping("/message")
    @SendTo("/chatroom/public")
    fun receiveMessage(@Payload message: Message): Message {
        println("connect WebSocket ${message.message}")
        return message
    }
}

data class Message (
    val message: String
)
