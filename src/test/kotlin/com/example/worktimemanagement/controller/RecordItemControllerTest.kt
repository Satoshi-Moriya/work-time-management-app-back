package com.example.worktimemanagement.controller

import com.example.worktimemanagement.entity.RecordItem
import com.example.worktimemanagement.service.RecordItemService
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
class RecordItemControllerTest {

    @Mock
    private lateinit var mockMvc: MockMvc

    @Mock
    private lateinit var mockRecordItemService: RecordItemService

    @InjectMocks
    private lateinit var recordItemController: RecordItemController

    @BeforeEach
    fun setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(recordItemController).build()
    }

    // GET「/record-items/{userId}」のテスト
    @Test
    fun `GET「／record-items／{userId}」のuserIdが1で呼ばれたとき、ステータス200が返ってくる（正常系）`() {
        mockMvc.perform(get("/record-items/1"))
            .andExpect(status().isOk)
    }

    @Test
    fun `GET「／record-items／{userId}」のuserIdがnullで呼ばれたとき、ステータス400が返ってくる（異常系＆バリデーションチェック）`() {
        mockMvc.perform(get("/record-items/null"))
            .andExpect(status().isBadRequest)
    }

    // POST「/record-items」のテスト
    @Test
    fun `POST「／record-items」が呼ばれたときにステータス201が返ってくる`() {
        val mockRecordItem = RecordItem(0,1, "稼働時間")
        val mapper = ObjectMapper()
        val json = mapper.writeValueAsString(mockRecordItem)

        mockMvc.perform(post("/record-items")
            .contentType(MediaType.APPLICATION_JSON)
            .content(json))
            .andExpect(status().isCreated)
    }

    @Test
    fun `POST「／record-items」が呼ばれたとき、createRecordItem()が実行されて、登録データが返ってくる`() {
        val mockRecordItem = RecordItem(0,1, "稼働時間")
        val mapper = ObjectMapper()
        val json = mapper.writeValueAsString(mockRecordItem)

        val expectedRecordItem = RecordItem(1,1, "稼働時間")
        `when`(mockRecordItemService.createRecordItem(mockRecordItem))
            .thenReturn(expectedRecordItem)

        mockMvc.perform(post("/record-items")
            .contentType(MediaType.APPLICATION_JSON)
            .content(json))
            .andExpect(jsonPath("$.recordItemId").value(1))
            .andExpect(jsonPath("$.userId").value(1))
            .andExpect(jsonPath("$.recordItemName").value("稼働時間"))

        verify(mockRecordItemService, times(1)).createRecordItem(mockRecordItem)
    }

    @Test
    fun `POST「／record-items」が呼ばれたとき、RequestBodyのRecordItemのrecordItemNameに空文字が入っている場合、ステータス400が返ってくる`() {
        val mockRecordItem = RecordItem(0,1, "")
        val mapper = ObjectMapper()
        val json = mapper.writeValueAsString(mockRecordItem)

        mockMvc.perform(post("/record-items")
            .contentType(MediaType.APPLICATION_JSON)
            .content(json))
            .andExpect(status().isBadRequest)
    }

    // Delete「/record-items/{recordItemId}」のテスト
    @Test
    fun `DELETE「／record-items／{recordItemId}」のrecordItemIdが1で呼ばれたとき、ステータス204が返ってくる（正常系）`() {
        mockMvc.perform(delete("/record-items/1"))
            .andExpect(status().isNoContent)
    }

    @Test
    fun `DELETE「／record-items／{recordItemId}」のrecordItemIdがnullで呼ばれたとき、ステータス400が返ってくる（異常系＆バリデーションチェック）`() {
        mockMvc.perform(delete("/record-items/null"))
            .andExpect(status().isBadRequest)
    }
}