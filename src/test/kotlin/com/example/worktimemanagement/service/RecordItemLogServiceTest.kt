package com.example.worktimemanagement.service

import com.example.worktimemanagement.entity.RecordItemLog
import com.example.worktimemanagement.repository.RecordItemLogRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.jupiter.MockitoExtension

@ExtendWith(MockitoExtension::class)
class RecordItemLogServiceTest {

    @Mock
    private lateinit var mockRecordItemLogRepository: RecordItemLogRepository

    @InjectMocks
    private lateinit var recordItemLogService: RecordItemLogServiceImpl

    // getSelectedMonthlyRecordItemLogのテスト
    @Test
    fun `getSelectedMonthlyRecordItemLog()が実行されると、recordItemLogRepositoryのfindByBetweenYearAndMonthが実行される`() {
        recordItemLogService.getSelectedMonthlyRecordItemLog(1, "20230601", "20230630")
        verify(mockRecordItemLogRepository, times(1)).findByBetweenYearAndMonth(1, "2023-06-01", "2023-06-30")
    }

    @Test
    fun `取得してきたRecordItemLogのrecordItemLogEndTimeに「yyyy-mm-dd 00 00 00」がある場合、「yyyy-mm-dd 24 00 00」に変換される`() {
        `when`(mockRecordItemLogRepository.findByBetweenYearAndMonth(1, "2023-06-01", "2023-06-30"))
            .thenReturn(listOf(
                RecordItemLog(1,1, "2023-06-03", "2023-06-03 23:30:00", "2023-06-04 00:00:00", 1800),
                RecordItemLog(2,1, "2023-06-04", "2023-06-04 00:00:00", "2023-06-04 01:00:00", 3600)
            ))

        val expectRecordItemLogList = listOf(
            RecordItemLog(1,1, "2023-06-03", "2023-06-03 23:30:00", "2023-06-03 24:00:00", 1800),
            RecordItemLog(2,1, "2023-06-04", "2023-06-04 00:00:00", "2023-06-04 01:00:00", 3600)
        )
        assertEquals(expectRecordItemLogList, recordItemLogService.getSelectedMonthlyRecordItemLog(1, "20230601", "20230630"))
    }

    @Test
    fun `取得してきたRecordItemLogのrecordItemLogEndTimeに「yyyy-mm-dd 00 00 00」がない場合、そのままである`() {
        `when`(mockRecordItemLogRepository.findByBetweenYearAndMonth(1, "2023-06-01", "2023-06-30"))
            .thenReturn(listOf(
                RecordItemLog(1,1, "2023-06-03", "2023-06-03 23:30:00", "2023-06-03 23:59:59", 1799),
                RecordItemLog(2,1, "2023-06-04", "2023-06-04 00:00:00", "2023-06-04 01:00:00", 3600)
            ))

        val expectRecordItemLogList = listOf(
            RecordItemLog(1,1, "2023-06-03", "2023-06-03 23:30:00", "2023-06-03 23:59:59", 1799),
            RecordItemLog(2,1, "2023-06-04", "2023-06-04 00:00:00", "2023-06-04 01:00:00", 3600)
        )
        assertEquals(expectRecordItemLogList, recordItemLogService.getSelectedMonthlyRecordItemLog(1, "20230601", "20230630"))
    }

    // createRecordItemLogのテスト
    @Test
    fun `createRecordItemLog()が実行されると、recordItemLogRepositoryのsaveAll()が実行される`() {
        val mockRecordItemLog = RecordItemLog(0,1, "2023-06-01", "2023-06-01 13:00:00", "2023-06-01 18:00:00", 18000)
        val recordItemLogList = listOf(mockRecordItemLog)
        recordItemLogService.createRecordItemLog(mockRecordItemLog)
        verify(mockRecordItemLogRepository, times(1)).saveAll(recordItemLogList)
    }

    @Test
    fun `引数で渡ってきたrecordItemLogのrecordItemEndTimeが「24 00 00」の場合、登録されるrecordItemLogのrecordItemEndTimeの日付が次の日になり、時間が「00 00 00」になっている`() {
        val mockRecordItemLog = RecordItemLog(0,1, "2023-06-01", "2023-06-01 19:00:00", "2023-06-01 24:00:00", 18000)
        val expectRecordItemLogList = listOf(RecordItemLog(0,1, "2023-06-01", "2023-06-01 19:00:00", "2023-06-02 00:00:00", 18000))
        recordItemLogService.createRecordItemLog(mockRecordItemLog)
        verify(mockRecordItemLogRepository, times(1)).saveAll(expectRecordItemLogList)
    }

    // deleteRecordItemLogのテスト
    @Test
    fun `deleteRecordItemLog()が実行されると、recordItemLogRepositoryのdeleteByRecordItemLogId()が実行される`() {
        val mockRecordItemLogId = 1
        recordItemLogService.deleteRecordItemLog(mockRecordItemLogId)
        verify(mockRecordItemLogRepository, times(1)).deleteByRecordItemLogId(mockRecordItemLogId)
    }
}
