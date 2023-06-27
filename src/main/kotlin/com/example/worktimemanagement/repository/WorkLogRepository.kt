package com.example.worktimemanagement.repository

import com.example.worktimemanagement.entity.WorkLog
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param

interface WorkLogRepository : CrudRepository<WorkLog, Int> {

    @Query("SELECT wl FROM WorkLog wl WHERE wl.workLogDate BETWEEN :startDate AND :endDate")
    fun findByBetweenYearAndMonth(@Param("startDate") startDate: String, @Param("endDate") endDate: String): List<WorkLog>
}