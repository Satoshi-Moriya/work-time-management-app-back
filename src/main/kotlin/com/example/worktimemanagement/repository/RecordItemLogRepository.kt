package com.example.worktimemanagement.repository

import com.example.worktimemanagement.entity.RecordItemLog
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.transaction.annotation.Transactional

@Transactional
interface RecordItemLogRepository : JpaRepository<RecordItemLog, Int> {

    @Query("SELECT ril FROM RecordItemLog ril WHERE ril.recordItemId = :recordItemId AND ril.recordItemLogDate BETWEEN :fromDate AND :toDate")
    fun findByBetweenYearAndMonth(@Param("recordItemId") recordItemId: Int, @Param("fromDate") fromDate: String, @Param("toDate") toDate: String): List<RecordItemLog>

    fun deleteByRecordItemLogId(recordItemLogId: Int)
}