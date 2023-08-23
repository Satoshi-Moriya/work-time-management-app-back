package com.example.worktimemanagement.repository

import com.example.worktimemanagement.entity.RecordItem
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.transaction.annotation.Transactional

@Transactional
interface RecordItemRepository : JpaRepository<RecordItem, Int> {

    fun findByUserId(userId: Int): List<RecordItem>

    fun deleteByRecordItemId(recordItemId: Int)
}