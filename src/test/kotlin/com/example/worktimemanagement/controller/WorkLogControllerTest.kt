package com.example.worktimemanagement.controller

import com.example.worktimemanagement.entity.WorkLog
import com.example.worktimemanagement.service.WorkLogService
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.test.web.servlet.setup.MockMvcBuilders

@ExtendWith(MockitoExtension::class)
@AutoConfigureMockMvc
class WorkLogControllerTest {

    @Mock
    private lateinit var mockMvc: MockMvc

    @Mock
    private lateinit var mockWorkLogService: WorkLogService

    @InjectMocks
    private lateinit var workLogController: WorkLogController

    @BeforeEach
    fun setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(workLogController).build()
    }

    @Test
    fun `GET「work-logs」が呼ばれたときにステータス200が返ってくる`() {
        mockMvc.perform(get("/work-logs/user-id/1")
            .param("from", "20230601")
            .param("to", "20230630"))
            .andExpect(status().isOk)
    }

    @Test
    fun `GET「work-logs」が呼ばれたときにfindByBetweenYearAndMonth()が実行されて、指定したuserと指定した年月のデータが返ってくる`() {

        `when`(mockWorkLogService.findByBetweenYearAndMonth(1,"20230601", "20230630"))
            .thenReturn(listOf(
                WorkLog(1,1, "2023-06-01", "2023-06-29 9:00:59", "2023-06-29 12:00:00", 10701),
                WorkLog(2,1, "2023-06-01", "2023-06-29 13:00:00", "2023-06-29 18:00:00", 18000)
            ))

        mockMvc.perform(get("/work-logs/user-id/1")
            .param("from", "20230601")
            .param("to", "20230630"))
            .andExpect(jsonPath("$[0].workLogId").value(1))
            .andExpect(jsonPath("$[0].workLogUserId").value(1))
            .andExpect(jsonPath("$[0].workLogDate").value("2023-06-01"))
            .andExpect(jsonPath("$[0].workLogStartTime").value("2023-06-29 9:00:59"))
            .andExpect(jsonPath("$[0].workLogEndTime").value("2023-06-29 12:00:00"))
            .andExpect(jsonPath("$[0].workLogSeconds").value(10701))
            .andExpect(jsonPath("$[1].workLogId").value(2))
            .andExpect(jsonPath("$[1].workLogUserId").value(1))
            .andExpect(jsonPath("$[1].workLogDate").value("2023-06-01"))
            .andExpect(jsonPath("$[1].workLogStartTime").value("2023-06-29 13:00:00"))
            .andExpect(jsonPath("$[1].workLogEndTime").value("2023-06-29 18:00:00"))
            .andExpect(jsonPath("$[1].workLogSeconds").value(18000));

        verify(mockWorkLogService, times(1)).findByBetweenYearAndMonth(1,"20230601", "20230630")
    }

    @Test
    fun `POST「work-log」が呼ばれたときにステータス200が返ってくる`() {
        val testWorkLog = WorkLog(0,1, "2023-07-03", "2023-07-03 9:00:59", "2023-07-03 12:00:00", 10701)
        val mapper = ObjectMapper()
        val json = mapper.writeValueAsString(testWorkLog)

        mockMvc.perform(post("/work-log")
            .contentType(MediaType.APPLICATION_JSON)
            .content(json))
            .andExpect(status().isCreated)
    }

    @Test
    fun `POST「work-log」が呼ばれたときにsaveAll()が実行されて、登録データが返ってくる`() {
        val testWorkLog = WorkLog(0,1, "2023-07-03", "2023-07-03 9:00:59", "2023-07-03 12:00:00", 10701)
        val mapper = ObjectMapper()
        val json = mapper.writeValueAsString(testWorkLog)

        val expectedWorkLog = listOf(WorkLog(1,1, "2023-07-03", "2023-07-03 9:00:59", "2023-07-03 12:00:00", 10701))
        `when`(mockWorkLogService.save(testWorkLog))
            .thenReturn(expectedWorkLog)

        mockMvc.perform(post("/work-log")
            .contentType(MediaType.APPLICATION_JSON)
            .content(json))
            .andExpect(jsonPath("$[0].workLogId").value(1))
            .andExpect(jsonPath("$[0].workLogUserId").value(1))
            .andExpect(jsonPath("$[0].workLogDate").value("2023-07-03"))
            .andExpect(jsonPath("$[0].workLogStartTime").value("2023-07-03 9:00:59"))
            .andExpect(jsonPath("$[0].workLogEndTime").value("2023-07-03 12:00:00"))
            .andExpect(jsonPath("$[0].workLogSeconds").value(10701))

        verify(mockWorkLogService, times(1)).save(testWorkLog)
    }

    @Test
    fun `2日間にわたるPOST「work-log」が呼ばれたときにsaveAll()が実行されて、登録データが返ってくる`() {
        val testWorkLog = WorkLog(0,1, "2023-07-03", "2023-07-03 23:30:00", "2023-07-04 01:00:00", 5400)
        val mapper = ObjectMapper()
        val json = mapper.writeValueAsString(testWorkLog)

        val expectedWorkLog = listOf(
            WorkLog(1,1, "2023-07-03", "2023-07-03 23:30:00", "2023-07-03 24:00:00", 1800),
            WorkLog(2,1, "2023-07-04", "2023-07-04 00:00:00", "2023-07-04 01:00:00", 3600),
        )
        `when`(mockWorkLogService.save(testWorkLog))
            .thenReturn(expectedWorkLog)

        mockMvc.perform(post("/work-log")
            .contentType(MediaType.APPLICATION_JSON)
            .content(json))
            .andExpect(jsonPath("$[0].workLogId").value(1))
            .andExpect(jsonPath("$[0].workLogUserId").value(1))
            .andExpect(jsonPath("$[0].workLogDate").value("2023-07-03"))
            .andExpect(jsonPath("$[0].workLogStartTime").value("2023-07-03 23:30:00"))
            .andExpect(jsonPath("$[0].workLogEndTime").value("2023-07-03 24:00:00"))
            .andExpect(jsonPath("$[0].workLogSeconds").value(1800))
            .andExpect(jsonPath("$[1].workLogId").value(2))
            .andExpect(jsonPath("$[1].workLogUserId").value(1))
            .andExpect(jsonPath("$[1].workLogDate").value("2023-07-04"))
            .andExpect(jsonPath("$[1].workLogStartTime").value("2023-07-04 00:00:00"))
            .andExpect(jsonPath("$[1].workLogEndTime").value("2023-07-04 01:00:00"))
            .andExpect(jsonPath("$[1].workLogSeconds").value(3600))

        verify(mockWorkLogService, times(1)).save(testWorkLog)
    }
}