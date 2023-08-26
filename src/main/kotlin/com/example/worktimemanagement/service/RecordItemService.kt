package com.example.worktimemanagement.service

import com.example.worktimemanagement.entity.RecordItem

import com.example.worktimemanagement.repository.RecordItemRepository
import org.springframework.stereotype.Service


interface RecordItemService {

    fun getRecordItems(userId: Int): List<RecordItem>

    fun createRecordItem(recordItem: RecordItem): RecordItem

    fun deleteRecordItem(recordItemId: Int)
}

@Service
class RecordItemServiceImpl(val recordItemRepository: RecordItemRepository): RecordItemService {

    override fun getRecordItems(userId: Int): List<RecordItem> {
        return recordItemRepository.findByUserId(userId)
    }

    override fun createRecordItem(recordItem: RecordItem): RecordItem {
        return recordItemRepository.save(recordItem)
    }

    override fun deleteRecordItem(recordItemId: Int) {
        recordItemRepository.deleteByRecordItemId(recordItemId)
    }
}