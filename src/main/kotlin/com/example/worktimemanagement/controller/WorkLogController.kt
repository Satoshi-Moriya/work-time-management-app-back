package com.example.worktimemanagement.controller

import com.example.worktimemanagement.entity.WorkLog
import com.example.worktimemanagement.service.WorkLogService
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

// テストリクエスト
// curl --location --request GET 'http://localhost:8080/work-logs/user-id/1?from=20230601&to=20230630'

@RestController
@CrossOrigin
class WorkLogController(val workLogService: WorkLogService) {

    @GetMapping("/work-logs/user-id/{userId}")
    fun getSelectedMonthlyWorkLog(@PathVariable userId: Int, @RequestParam("from") fromDate: String, @RequestParam("to") toDate: String): List<WorkLog> {
        return workLogService.findByBetweenYearAndMonth(userId, fromDate, toDate)
    }
}