import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.handler.annotation.SendTo
import org.springframework.stereotype.Controller

data class Stopwatch(var elapsedTime: Long = 0L, var isRunning: Boolean = false)

@SpringBootApplication
class Application

@Controller
class StopwatchControllerExm {
    private val stopwatchMap: MutableMap<String, Stopwatch> = HashMap()

    @MessageMapping("/start/{userId}")
    @SendTo("/topic/stopwatch/{userId}")
    fun startStopwatch(userId: String): Stopwatch {
        val stopwatch = getOrCreateStopwatch(userId)
        if (!stopwatch.isRunning) {
            stopwatch.isRunning = true
            updateStopwatch(userId)
        }
        return stopwatch
    }

    @MessageMapping("/stop/{userId}")
    @SendTo("/topic/stopwatch/{userId}")
    fun stopStopwatch(userId: String): Stopwatch {
        val stopwatch = getOrCreateStopwatch(userId)
        stopwatch.isRunning = false
        return stopwatch
    }

    @MessageMapping("/reset/{userId}")
    @SendTo("/topic/stopwatch/{userId}")
    fun resetStopwatch(userId: String): Stopwatch {
        val stopwatch = getOrCreateStopwatch(userId)
        stopwatch.elapsedTime = 0L
        stopwatch.isRunning = false
        return stopwatch
    }

    private fun updateStopwatch(userId: String) {
        Thread {
            val stopwatch = stopwatchMap[userId]
            while (stopwatch?.isRunning == true) {
                Thread.sleep(10L) // タイマーの更新間隔（ここでは10ミリ秒）を設定します
                stopwatch.elapsedTime += 10L
            }
        }.start()
    }

    private fun getOrCreateStopwatch(userId: String): Stopwatch {
        return stopwatchMap.getOrPut(userId) { Stopwatch() }
    }
}

fun main(args: Array<String>) {
    runApplication<Application>(*args)
}
