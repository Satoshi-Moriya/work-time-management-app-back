package com.example.worktimemanagement.repository

import com.example.worktimemanagement.entity.WorkLog
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param

interface WorkLogRepository : CrudRepository<WorkLog, Int> {

    @Query("SELECT wl FROM WorkLog wl WHERE wl.workLogDate BETWEEN :fromDate AND :toDate")
    fun findByBetweenYearAndMonth(@Param("fromDate") fromDate: String, @Param("toDate") toDate: String): List<WorkLog>
}