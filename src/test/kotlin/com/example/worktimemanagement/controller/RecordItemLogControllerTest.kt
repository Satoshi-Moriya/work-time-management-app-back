package com.example.worktimemanagement.controller

import com.example.worktimemanagement.entity.RecordItemLog
import com.example.worktimemanagement.service.RecordItemLogService
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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.test.web.servlet.setup.MockMvcBuilders

@ExtendWith(MockitoExtension::class)
@AutoConfigureMockMvc
class RecordItemLogControllerTest {

    @Mock
    private lateinit var mockMvc: MockMvc

    @Mock
    private lateinit var mockRecordItemLogService: RecordItemLogService

    @InjectMocks
    private lateinit var recordItemLogController: RecordItemLogController

    @BeforeEach
    fun setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(recordItemLogController).build()
    }

    // GET「/record-item-logs/{recordItemId}」のテスト
    @Test
    fun `GET「／record-item-logs／{recordItemId}」のrecordItemIdが1で呼ばれたとき、ステータス200が返ってくる（正常系）`() {
        mockMvc.perform(get("/record-item-logs/1")
            .param("from", "20230601")
            .param("to", "20230630"))
            .andExpect(status().isOk)
    }

    @Test
    fun `GET「／record-item-logs／{recordItemId}」のrecordItemIdがnullで呼ばれたとき、ステータス400が返ってくる（異常系＆バリデーションチェック）`() {
        mockMvc.perform(get("/record-item-logs/null")
            .param("from", "20230601")
            .param("to", "20230630"))
            .andExpect(status().isBadRequest)
    }

    // POST「/record-item-logs」のテスト
    @Test
    fun `POST「／record-item-logs」が呼ばれたときにステータス201が返ってくる`() {
        val mockRecordItemLog = RecordItemLog(0,1, "2023-07-03", "2023-07-03 09:00:59", "2023-07-03 12:00:00", 10701)
        val mapper = ObjectMapper()
        val json = mapper.writeValueAsString(mockRecordItemLog)

        mockMvc.perform(post("/record-item-logs")
            .contentType(MediaType.APPLICATION_JSON)
            .content(json))
            .andExpect(status().isCreated)
    }

    @Test
    fun `POST「／record-item-logs」が呼ばれたとき、createRecordItemLog()が実行されて、登録データが返ってくる`() {
        val mockRecordItemLog = RecordItemLog(0,1, "2023-07-03", "2023-07-03 09:00:59", "2023-07-03 12:00:00", 10701)
        val mapper = ObjectMapper()
        val json = mapper.writeValueAsString(mockRecordItemLog)

        val expectedRecordItemLog = listOf(RecordItemLog(1,1, "2023-07-03", "2023-07-03 09:00:59", "2023-07-03 12:00:00", 10701))
        `when`(mockRecordItemLogService.createRecordItemLog(mockRecordItemLog))
            .thenReturn(expectedRecordItemLog)

        mockMvc.perform(post("/record-item-logs")
            .contentType(MediaType.APPLICATION_JSON)
            .content(json))
            .andExpect(jsonPath("$[0].recordItemLogId").value(1))
            .andExpect(jsonPath("$[0].recordItemId").value(1))
            .andExpect(jsonPath("$[0].recordItemLogDate").value("2023-07-03"))
            .andExpect(jsonPath("$[0].recordItemLogStartTime").value("2023-07-03 09:00:59"))
            .andExpect(jsonPath("$[0].recordItemLogEndTime").value("2023-07-03 12:00:00"))
            .andExpect(jsonPath("$[0].recordItemLogSeconds").value(10701))

        verify(mockRecordItemLogService, times(1)).createRecordItemLog(mockRecordItemLog)
    }

    @Test
    fun `POST「／record-item-logs」が呼ばれたとき、RequestBodyのRecordItemLogのrecordItemLogDateに空文字が入っている場合、ステータス400が返ってくる`() {
        val mockRecordItemLog = RecordItemLog(0,1, "", "2023-07-03 09:00:59", "2023-07-03 12:00:00", 10701)
        val mapper = ObjectMapper()
        val json = mapper.writeValueAsString(mockRecordItemLog)

        mockMvc.perform(post("/record-item-logs")
            .contentType(MediaType.APPLICATION_JSON)
            .content(json))
            .andExpect(status().isBadRequest)
    }

    @Test
    fun `POST「／record-item-logs」が呼ばれたとき、RequestBodyのRecordItemLogのrecordItemLogDateの年のフォーマットが崩れていた場合、ステータス400が返ってくる`() {
        val mockRecordItemLog = RecordItemLog(0,1, "223-07-03", "2023-07-03 09:00:59", "2023-07-03 12:00:00", 10701)
        val mapper = ObjectMapper()
        val json = mapper.writeValueAsString(mockRecordItemLog)

        mockMvc.perform(post("/record-item-logs")
            .contentType(MediaType.APPLICATION_JSON)
            .content(json))
            .andExpect(status().isBadRequest)
    }

    @Test
    fun `POST「／record-item-logs」が呼ばれたとき、RequestBodyのRecordItemLogのrecordItemLogDateの月が13の場合、ステータス400が返ってくる`() {
        val mockRecordItemLog = RecordItemLog(0,1, "2023-13-03", "2023-07-03 09:00:59", "2023-07-03 12:00:00", 10701)
        val mapper = ObjectMapper()
        val json = mapper.writeValueAsString(mockRecordItemLog)

        mockMvc.perform(post("/record-item-logs")
            .contentType(MediaType.APPLICATION_JSON)
            .content(json))
            .andExpect(status().isBadRequest)
    }

    @Test
    fun `POST「／record-item-logs」が呼ばれたとき、RequestBodyのRecordItemLogのrecordItemLogDateの月のフォーマットが崩れていた場合、ステータス400が返ってくる`() {
        val mockRecordItemLog = RecordItemLog(0,1, "2023-7-03", "2023-07-03 09:00:59", "2023-07-03 12:00:00", 10701)
        val mapper = ObjectMapper()
        val json = mapper.writeValueAsString(mockRecordItemLog)

        mockMvc.perform(post("/record-item-logs")
            .contentType(MediaType.APPLICATION_JSON)
            .content(json))
            .andExpect(status().isBadRequest)
    }

    @Test
    fun `POST「／record-item-logs」が呼ばれたとき、RequestBodyのRecordItemLogのrecordItemLogDateの日が32の場合、ステータス400が返ってくる`() {
        val mockRecordItemLog = RecordItemLog(0,1, "2023-07-32", "2023-07-03 09:00:59", "2023-07-03 12:00:00", 10701)
        val mapper = ObjectMapper()
        val json = mapper.writeValueAsString(mockRecordItemLog)

        mockMvc.perform(post("/record-item-logs")
            .contentType(MediaType.APPLICATION_JSON)
            .content(json))
            .andExpect(status().isBadRequest)
    }

    @Test
    fun `POST「／record-item-logs」が呼ばれたとき、RequestBodyのRecordItemLogのrecordItemLogDateの日のフォーマットが崩れていた場合、ステータス400が返ってくる`() {
        val mockRecordItemLog = RecordItemLog(0,1, "2023-07-3", "2023-07-03 09:00:59", "2023-07-03 12:00:00", 10701)
        val mapper = ObjectMapper()
        val json = mapper.writeValueAsString(mockRecordItemLog)

        mockMvc.perform(post("/record-item-logs")
            .contentType(MediaType.APPLICATION_JSON)
            .content(json))
            .andExpect(status().isBadRequest)
    }

    // recordItemLogStartTimeとrecordItemLogEndTimeはUserのcreatedAtなどと同じPatternなのでテストなし

    // Delete「/record-item-logs/{recordItemId}」のテスト
    @Test
    fun `DELETE「／record-item-logs／{recordItemId}」のrecordItemIdが1で呼ばれたとき、ステータス204が返ってくる（正常系）`() {
        mockMvc.perform(delete("/record-item-logs/1"))
            .andExpect(status().isNoContent)
    }

    @Test
    fun `DELETE「／record-item-logs／{recordItemId}」のrecordItemIdがnullで呼ばれたとき、ステータス400が返ってくる（異常系＆バリデーションチェック）`() {
        mockMvc.perform(delete("/record-item-logs/null"))
            .andExpect(status().isBadRequest)
    }
}