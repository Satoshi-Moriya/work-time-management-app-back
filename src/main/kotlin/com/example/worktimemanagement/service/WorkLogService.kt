package com.example.worktimemanagement.service

import com.example.worktimemanagement.entity.WorkLog
import com.example.worktimemanagement.repository.WorkLogRepository
import org.springframework.stereotype.Service

const val INSERT_HYPHEN_POSITION1 = 4
const val INSERT_HYPHEN_POSITION2 = 7

@Service
class WorkLogService(val workLogRepository: WorkLogRepository) {

    fun findByBetweenYearAndMonth(userId: Int, fromDate: String, toDate: String): List<WorkLog> {
        val modifiedFromDate = StringBuilder(fromDate)
            .apply {
                insert(INSERT_HYPHEN_POSITION1, "-")
                insert(INSERT_HYPHEN_POSITION2, "-")
            }.toString()
        val modifiedToDate = StringBuilder(toDate)
            .apply {
                insert(INSERT_HYPHEN_POSITION1, "-")
                insert(INSERT_HYPHEN_POSITION2, "-")
            }.toString()
        return workLogRepository.findByBetweenYearAndMonth(userId, modifiedFromDate, modifiedToDate)
    }
}