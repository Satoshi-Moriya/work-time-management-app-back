package com.example.worktimemanagement.service

import com.example.worktimemanagement.entity.WorkLog
import com.example.worktimemanagement.repository.WorkLogRepository
import org.springframework.stereotype.Service

const val INSERT_HYPHEN_POSITION1 = 4
const val INSERT_HYPHEN_POSITION2 = 7

@Service
class WorkLogService(val workLogRepository: WorkLogRepository) {

    fun findByBetweenYearAndMonth(startDate: String, endDate: String): List<WorkLog> {
        val modifiedStartDate = StringBuilder(startDate)
            .apply {
                insert(INSERT_HYPHEN_POSITION1, "-")
                insert(INSERT_HYPHEN_POSITION2, "-")
            }.toString()
        val modifiedEndDate = StringBuilder(endDate)
            .apply {
                insert(INSERT_HYPHEN_POSITION1, "-")
                insert(INSERT_HYPHEN_POSITION2, "-")
            }.toString()
        return workLogRepository.findByBetweenYearAndMonth(modifiedStartDate, modifiedEndDate)
    }
}