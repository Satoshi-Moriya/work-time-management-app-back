package com.example.worktimemanagement.controller

import com.example.worktimemanagement.service.StopwatchService
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.stereotype.Controller
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.concurrent.ConcurrentHashMap

// テストリクエスト
// curl --location --request GET 'http://localhost:8080/stopwatch/start or stop/{user-id}

@Controller
class StopwatchController (private val simpleMessagingTemplate: SimpMessagingTemplate, val stopwatchService: StopwatchService) {

    private val stopwatchMap: MutableMap<Int, Stopwatch> = HashMap()
    private val stopwatchThreads: MutableMap<Int, Thread> = ConcurrentHashMap()

    @MessageMapping("/start/{userId}")
    fun startStopwatch(@Payload stopwatch: Stopwatch) {
        val selectedUserStopwatch = getOrCreateStopwatch(stopwatch.userId)
        selectedUserStopwatch.userId = stopwatch.userId
        selectedUserStopwatch.date = getExecutionDate()
        println(stopwatch)

        if (!selectedUserStopwatch.isRunning) {
            selectedUserStopwatch.isRunning = true
            updateStopwatch(stopwatch.userId)
        }
    }

    @MessageMapping("/stop/{userId}")
    fun stopStopwatch(@Payload stopwatch: Stopwatch) {
        val selectedUserStopwatch = getOrCreateStopwatch(stopwatch.userId)
        selectedUserStopwatch.isRunning = false
        stopwatchThreads[stopwatch.userId]?.interrupt()
        stopwatchThreads.remove(stopwatch.userId)
        // 測った時間をworkLogテーブルに登録処理
//        stopwatchService.registerWorkLog(selectedUserStopwatch)

        selectedUserStopwatch.elapsedTime = 0L
        simpleMessagingTemplate.convertAndSend("/display/stopwatch/${stopwatch.userId}", stopwatch)
    }


    private fun updateStopwatch(userId: Int) {
        val thread = Thread {
            try {
                val stopwatch = stopwatchMap[userId]
                stopwatch?.startTime = System.currentTimeMillis()
                while (stopwatch?.isRunning == true && !Thread.currentThread().isInterrupted) {
                    Thread.sleep(1000)
                    stopwatch.elapsedTime += 1L
                    simpleMessagingTemplate.convertAndSend("/display/stopwatch/${userId}", stopwatch)
                    println(stopwatch)
                }
            } catch (e: InterruptedException) {
                println("ストップウォッチがストップされました。")
                println("残っているスレッド : ${stopwatchThreads.values}")
            }

        }

        stopwatchThreads[userId] = thread
        thread.start()
    }

    private fun getOrCreateStopwatch(userId: Int): Stopwatch {
        return stopwatchMap.getOrPut(userId) { Stopwatch() }
    }
}

data class Stopwatch (
    var userId: Int = 0,
    var date: String = "",
    var startTime: Long = 0L,
    var endTime: Long = 0L,
    var elapsedTime: Long = 0L,
    var isRunning: Boolean = false,
)
private fun getExecutionDate(): String {
    val date = LocalDateTime.now()
    val dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    return dateFormat.format(date)
}

