package com.example.worktimemanagement.controller

import com.example.worktimemanagement.entity.WorkLog
import com.example.worktimemanagement.service.WorkLogService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

// テストリクエスト
// curl --location --request GET 'http://localhost:8080/worklogs?start=20230601&end=20230630'

@RestController
class WorkLogController(val workLogService: WorkLogService) {

    @GetMapping("/work-logs")
    fun getSelectedMonthlyWorkLog(@RequestParam("start") startDate: String, @RequestParam("end") endDate: String): List<WorkLog> {
        return workLogService.findByBetweenYearAndMonth(startDate, endDate)
    }
}