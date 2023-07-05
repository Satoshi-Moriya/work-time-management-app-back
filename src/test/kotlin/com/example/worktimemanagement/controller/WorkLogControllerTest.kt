package com.example.worktimemanagement.controller

import com.example.worktimemanagement.entity.WorkLog
import com.example.worktimemanagement.service.WorkLogService
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mockito.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@ExtendWith(SpringExtension::class)
@SpringBootTest
@AutoConfigureMockMvc
class WorkLogControllerTest {

    @Autowired
    lateinit var mockMvc: MockMvc
    @MockBean
    lateinit var mockWorkLogService: WorkLogService

    @Test
    fun `api「work-logs」を呼んだときにステータス200が返る`() {
        mockMvc.perform(get("/work-logs/user-id/1")
            .param("from", "20230601")
            .param("to", "20230630"))
            .andExpect(status().isOk)
    }

    @Test
    fun `クエリパラメータにfromとtoの数値を入れた時、getSelectedMonthlyWorkLogにそれぞれの数値が引数に入り実行されている`() {

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
}