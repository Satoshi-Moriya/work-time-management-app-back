package com.example.worktimemanagement.controller

import com.example.worktimemanagement.entity.WorkLog
import com.example.worktimemanagement.service.WorkLogService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@CrossOrigin
class WorkLogController(val workLogService: WorkLogService) {

    @GetMapping("/work-log/users/{userId}")
    @ResponseStatus(HttpStatus.OK)
    fun getSelectedMonthlyWorkLog(@PathVariable userId: Int, @RequestParam("from") fromDate: String, @RequestParam("to") toDate: String): List<WorkLog> {
        return workLogService.findByBetweenYearAndMonth(userId, fromDate, toDate)
    }

    @PostMapping("/work-log")
    @ResponseStatus(HttpStatus.CREATED)
    fun save(@RequestBody workLogBody: WorkLog): List<WorkLog> {
        return workLogService.save(workLogBody)
    }
}