package com.example.worktimemanagement.controller

import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.handler.annotation.SendTo
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin

// テストリクエスト
// curl --location --request GET 'http://localhost:8080/stopwatch/start or stop/{user-id}

data class Stopwatch(var elapsedTime: Int = 0, var isRunning: Boolean = false)

@Controller
@CrossOrigin
class StopwatchController {

    private val stopwatchMap: MutableMap<String, Stopwatch> = HashMap()

    @MessageMapping("/stopwatch/start/{userId}")
    @SendTo("/topic/stopwatch/{userId}")
    fun startStopwatch(userId: String): Stopwatch {
        val stopwatch = getOrCreateStopwatch(userId)
        if (!stopwatch.isRunning) {
            stopwatch.isRunning = true
            updateStopwatch(userId)
        }
        return stopwatch
    }

    @MessageMapping("/stopwatch/stop/{userId}")
    @SendTo("/topic/stopwatch/{userId}")
    fun stopStopwatch(userId: String): Stopwatch {
        val stopwatch = getOrCreateStopwatch(userId)
        stopwatch.isRunning = false
        return  stopwatch
    }


    private fun updateStopwatch(userId: String) {
        Thread {
            val stopwatch = stopwatchMap[userId]
            while (stopwatch?.isRunning == true) {
                Thread.sleep(1000)
                stopwatch.elapsedTime += 1000
            }
        }.start()
    }

    private fun getOrCreateStopwatch(userId: String): Stopwatch {
        return stopwatchMap.getOrPut(userId) { Stopwatch() }
    }
}