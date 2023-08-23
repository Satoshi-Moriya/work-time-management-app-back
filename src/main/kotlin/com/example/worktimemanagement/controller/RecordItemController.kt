package com.example.worktimemanagement.controller

import com.example.worktimemanagement.entity.RecordItem
import com.example.worktimemanagement.service.RecordItemService
import jakarta.validation.constraints.NotNull
import org.springframework.http.HttpStatus
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*

@RestController
@CrossOrigin
class RecordItemController(val recordItemService: RecordItemService) {

    @GetMapping("/record-items/{userId}")
    @ResponseStatus(HttpStatus.OK)
    fun getRecordItems(@PathVariable @NotNull userId: Int): List<RecordItem> {
        return recordItemService.getRecordItems(userId)
    }

    @PostMapping("/record-items")
    @ResponseStatus(HttpStatus.CREATED)
    fun createRecordItem(@RequestBody @Validated recordItem: RecordItem): RecordItem {
        return recordItemService.createRecordItem(recordItem)
    }

    @DeleteMapping("/record-items/{recordItemId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteRecordItem(@PathVariable @NotNull recordItemId: Int) {
        recordItemService.deleteRecordItem(recordItemId)
    }
}