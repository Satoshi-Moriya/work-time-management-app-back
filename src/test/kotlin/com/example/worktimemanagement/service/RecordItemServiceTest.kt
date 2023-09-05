package com.example.worktimemanagement.service

import com.example.worktimemanagement.entity.RecordItem
import com.example.worktimemanagement.repository.RecordItemRepository
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.jupiter.MockitoExtension

@ExtendWith(MockitoExtension::class)
class RecordItemServiceTest {

    @Mock
    private lateinit var mockRecordItemRepository: RecordItemRepository

    @InjectMocks
    private lateinit var recordItemService: RecordItemServiceImpl

    // getRecordItemsのテスト
    @Test
    fun `getRecordItems()が実行されると、recordItemRepositoryのfindByUserIdが実行される`() {
        recordItemService.getRecordItems(1)
        verify(mockRecordItemRepository, times(1)).findByUserId(1)
    }

    // createRecordItemのテスト
    @Test
    fun `createRecordItem()が実行されると、recordItemRepositoryのsave()が実行される`() {
        val mockRecordItem = RecordItem(0,1, "稼働時間")
        `when`(mockRecordItemRepository.save(mockRecordItem))
            .thenReturn(RecordItem(1, 1, "稼働時間"))

        recordItemService.createRecordItem(mockRecordItem)

        verify(mockRecordItemRepository, times(1)).save(mockRecordItem)
    }

    // deleteRecordItemのテスト
    @Test
    fun `deleteRecordItem()が実行されると、recordItemRepositoryのdeleteByRecordItemId()が実行される`() {
        val mockRecordItemId = 1
        recordItemService.deleteRecordItem(mockRecordItemId)
        verify(mockRecordItemRepository, times(1)).deleteByRecordItemId(mockRecordItemId)
    }
}
