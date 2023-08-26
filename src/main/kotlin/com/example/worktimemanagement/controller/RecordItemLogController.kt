package com.example.worktimemanagement.controller

import com.example.worktimemanagement.entity.RecordItemLog
import com.example.worktimemanagement.service.RecordItemLogService
import jakarta.validation.constraints.NotNull
import org.springframework.http.HttpStatus
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*

@RestController
@CrossOrigin
class RecordItemLogController(val recordItemLogService: RecordItemLogService) {

    @GetMapping("/record-item-logs/{recordItemId}")
    @ResponseStatus(HttpStatus.OK)
    fun getSelectedMonthlyRecordItemLog(@PathVariable @NotNull recordItemId: Int, @RequestParam("from") fromDate: String, @RequestParam("to") toDate: String): List<RecordItemLog> {
        return recordItemLogService.getSelectedMonthlyRecordItemLog(recordItemId, fromDate, toDate)
    }

    @PostMapping("/record-item-logs")
    @ResponseStatus(HttpStatus.CREATED)
    fun createRecordItemLog(@RequestBody @Validated recordItemLog: RecordItemLog): List<RecordItemLog> {
        return recordItemLogService.createRecordItemLog(recordItemLog)
    }

    @DeleteMapping("/record-item-logs/{recordItemLogId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteRecordItemLog(@PathVariable @NotNull recordItemLogId: Int) {
        recordItemLogService.deleteRecordItemLog(recordItemLogId)
    }
}