package com.example.worktimemanagement.service

import com.example.worktimemanagement.entity.WorkLog
import com.example.worktimemanagement.repository.WorkLogRepository
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.junit.jupiter.api.Assertions.*
import org.mockito.Mockito.times
import org.mockito.Mockito.verify


@SpringBootTest
class WorkLogServiceTest {

    @Autowired
    private lateinit var workLogService: WorkLogService

    @MockBean
    private lateinit var mockWorkLogRepository: WorkLogRepository

    @Test
    fun `save()が実行されると、workRepositoryのsaveAll()が実行されている`() {
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
