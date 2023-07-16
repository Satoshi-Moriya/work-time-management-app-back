package com.example.worktimemanagement.controller

import com.example.worktimemanagement.entity.WorkLog
import com.example.worktimemanagement.service.WorkLogService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@CrossOrigin
class WorkLogController(val workLogService: WorkLogService) {

    // テストリクエスト
    // curl --location --request GET 'http://localhost:8080/work-logs/user-id/1?from=20230601&to=20230630'
    @GetMapping("/work-logs/user-id/{userId}")
    fun getSelectedMonthlyWorkLog(@PathVariable userId: Int, @RequestParam("from") fromDate: String, @RequestParam("to") toDate: String): List<WorkLog> {
        return workLogService.findByBetweenYearAndMonth(userId, fromDate, toDate)
    }

    // テストリクエスト
    // curl --location --request POST 'http://localhost:8080/work-log' \
    // --header 'Content-Type: application/json' \
    // --data-raw '{"workLogUserId" : "1", "workLogDate" : "2023-07-03", "workLogStartTime" : "2023-07-03 9:00:59", "workLogEndTime" : "2023-07-03 12:00:00", "workLogSeconds" : "10741"}' | jq
    @PostMapping("/work-log")
    @ResponseStatus(HttpStatus.CREATED)
    fun save(@RequestBody workLogBody: WorkLog): WorkLog {
        println(workLogBody)
        return workLogService.save(workLogBody)
    }
}