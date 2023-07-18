package com.example.worktimemanagement.service

import com.example.worktimemanagement.entity.WorkLog
import com.example.worktimemanagement.repository.WorkLogRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.jupiter.MockitoExtension

@ExtendWith(MockitoExtension::class)
class WorkLogServiceTest {

    @Mock
    private lateinit var mockWorkLogRepository: WorkLogRepository

    @InjectMocks
    private lateinit var workLogService: WorkLogServiceImpl

    @Test
    fun `findByBetweenYearAndMonth()が実行されると、workRepositoryのfindByBetweenYearAndMonthが実行される`() {
        workLogService.findByBetweenYearAndMonth(1, "20230601", "20230630")
        verify(mockWorkLogRepository, times(1)).findByBetweenYearAndMonth(1, "2023-06-01", "2023-06-30")
    }

    @Test
    fun `取得してきたWorkLogのList内のworkLogEndTimeに「yyyy-mm-dd 00 00 00」がある場合、「yyyy-mm-dd 24 00 00」に変換される`() {
        `when`(mockWorkLogRepository.findByBetweenYearAndMonth(1, "2023-06-01", "2023-06-30"))
            .thenReturn(listOf(
                WorkLog(1,1, "2023-06-03", "2023-06-03 23:30:00", "2023-06-04 00:00:00", 1800),
                WorkLog(2,1, "2023-06-04", "2023-06-04 00:00:00", "2023-06-04 01:00:00", 3600)
            ))

        val expectWorkLogList = listOf(
            WorkLog(1,1, "2023-06-03", "2023-06-03 23:30:00", "2023-06-03 24:00:00", 1800),
            WorkLog(2,1, "2023-06-04", "2023-06-04 00:00:00", "2023-06-04 01:00:00", 3600)
        )
        assertEquals(expectWorkLogList, workLogService.findByBetweenYearAndMonth(1, "20230601", "20230630"))
    }

    @Test
    fun `取得してきたWorkLogのList内のworkLogEndTimeに「yyyy-mm-dd 00 00 00」がない場合、そのままである`() {
        `when`(mockWorkLogRepository.findByBetweenYearAndMonth(1, "2023-06-01", "2023-06-30"))
            .thenReturn(listOf(
                WorkLog(1,1, "2023-06-03", "2023-06-03 23:30:00", "2023-06-03 23:59:59", 1799),
                WorkLog(2,1, "2023-06-04", "2023-06-04 00:00:00", "2023-06-04 01:00:00", 3600)
            ))

        val expectWorkLogList = listOf(
            WorkLog(1,1, "2023-06-03", "2023-06-03 23:30:00", "2023-06-03 23:59:59", 1799),
            WorkLog(2,1, "2023-06-04", "2023-06-04 00:00:00", "2023-06-04 01:00:00", 3600)
        )
        assertEquals(expectWorkLogList, workLogService.findByBetweenYearAndMonth(1, "20230601", "20230630"))
    }

    @Test
    fun `save()が実行されると、workRepositoryのsaveAll()が実行される`() {
        val workLog = WorkLog(0,1, "2023-06-01", "2023-06-01 13:00:00", "2023-06-01 18:00:00", 18000)
        val workLogList = arrayListOf(workLog)
        workLogService.save(workLog)
        verify(mockWorkLogRepository, times(1)).saveAll(workLogList)
    }

    @Test
    fun `引数で渡ってきたworkLogのworkLogStartTimeとworkLogEndTimeの日付が同じ場合、1つのWorkLogのになっている`() {
        val testWorkLog = WorkLog(0,1, "2023-06-01", "2023-06-01 13:00:00", "2023-06-01 18:00:00", 18000)
        val expectWorkLogList = arrayListOf(WorkLog(0,1, "2023-06-01", "2023-06-01 13:00:00", "2023-06-01 18:00:00", 18000))
        assertEquals(expectWorkLogList, workLogService.save(testWorkLog))
    }

    @Test
    fun `引数で渡ってきたworkLogのworkLogStartTimeとworkLogEndTimeの日付が1日違う場合、2つのWorkLogになっている`() {
        val testWorkLog = WorkLog(0,1, "2023-06-01", "2023-06-01 23:00:00", "2023-06-02 01:30:00", 9000)
        val expectWorkLogList = arrayListOf(
            WorkLog(0,1, "2023-06-01", "2023-06-01 23:00:00", "2023-06-02 00:00:00", 3600),
            WorkLog(0,1, "2023-06-02", "2023-06-02 00:00:00", "2023-06-02 01:30:00", 5400)
        )
        assertEquals(expectWorkLogList, workLogService.save(testWorkLog))
    }

    @Test
    fun `引数で渡ってきたworkLogのworkLogStartTimeとworkLogEndTimeの日付が2日違う場合、3つのWorkLogになっている`() {
        val testWorkLog = WorkLog(0,1, "2023-06-01", "2023-06-01 23:00:00", "2023-06-03 01:30:00", 95400)
        val expectWorkLogList = arrayListOf(
            WorkLog(0,1, "2023-06-01", "2023-06-01 23:00:00", "2023-06-02 00:00:00", 3600),
            WorkLog(0,1, "2023-06-02", "2023-06-02 00:00:00", "2023-06-03 00:00:00", 86400),
            WorkLog(0,1, "2023-06-03", "2023-06-03 00:00:00", "2023-06-03 01:30:00", 5400)
        )
        assertEquals(expectWorkLogList, workLogService.save(testWorkLog))
    }

    @Test
    fun `引数で渡ってきたworkLogのworkLogStartTimeとworkLogEndTimeの日付の年が違う場合でも、2つのWorkLogになっている`() {
        val testWorkLog = WorkLog(0,1, "2022-12-31", "2022-12-31 23:00:00", "2023-01-01 01:30:00", 9000)
        val expectWorkLogList = arrayListOf(
            WorkLog(0,1, "2022-12-31", "2022-12-31 23:00:00", "2023-01-01 00:00:00", 3600),
            WorkLog(0,1, "2023-01-01", "2023-01-01 00:00:00", "2023-01-01 01:30:00", 5400)
        )
        assertEquals(expectWorkLogList, workLogService.save(testWorkLog))
    }

    @Test
    fun `引数で渡ってきたworkLogのworkLogStartTimeとworkLogEndTimeの日付が閏年で2日違う場合、3つのWorkLogになっている`() {
        val testWorkLog = WorkLog(0,1, "2024-02-28", "2024-02-28 23:00:00", "2024-03-01 01:30:00", 95400)
        val expectWorkLogList = arrayListOf(
            WorkLog(0,1, "2024-02-28", "2024-02-28 23:00:00", "2024-02-29 00:00:00", 3600),
            WorkLog(0,1, "2024-02-29", "2024-02-29 00:00:00", "2024-03-01 00:00:00", 86400),
            WorkLog(0,1, "2024-03-01", "2024-03-01 00:00:00", "2024-03-01 01:30:00", 5400)
        )
        assertEquals(expectWorkLogList, workLogService.save(testWorkLog))
    }

    @Test
    fun `引数で渡ってきたworkLogのworkLogStartTimeとworkLogEndTimeの日付が閏年ではない2月の終わりの場合、2つのWorkLogになっている`() {
        val testWorkLog = WorkLog(0,1, "2023-02-28", "2023-02-28 23:00:00", "2023-03-01 01:30:00", 9000)
        val expectWorkLogList = arrayListOf(
            WorkLog(0,1, "2023-02-28", "2023-02-28 23:00:00", "2023-03-01 00:00:00", 3600),
            WorkLog(0,1, "2023-03-01", "2023-03-01 00:00:00", "2023-03-01 01:30:00", 5400)
        )
        assertEquals(expectWorkLogList, workLogService.save(testWorkLog))
    }
}
