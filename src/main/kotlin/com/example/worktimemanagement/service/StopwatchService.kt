package com.example.worktimemanagement.service

import com.example.worktimemanagement.controller.Stopwatch
import com.example.worktimemanagement.entity.WorkLog
import com.example.worktimemanagement.repository.WorkLogRepository
import org.springframework.stereotype.Service

@Service
class StopwatchService(val workLogRepository: WorkLogRepository) {

    fun registerWorkLog(stopwatch: Stopwatch) {
        println(stopwatch)
        // 日付を跨ぐ場合があるからsaveAll
//        val saveWorkLog =
//            listOf(
//                WorkLog(null, stopwatch.userId, stopwatch.date, stopwatch.startTime, stopwatch.endTime, stopwatch.elapsedTime),
//                WorkLog(null, stopwatch.userId, stopwatch.date, stopwatch.startTime, stopwatch.endTime, stopwatch.elapsedTime),
//            )
//        workLogRepository.saveAll(saveWorkLog)
    }

}