package com.example.worktimemanagement.controller

import com.example.worktimemanagement.dto.CustomResponse
import com.example.worktimemanagement.service.UserService
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.*
import org.springframework.http.MediaType
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import java.util.*

@ExtendWith(SpringExtension::class)
// ToDo 下記設定がないとMockitoでむしろスタブされる意味がよくわからん
//@ExtendWith(MockitoExtension::class)
@AutoConfigureMockMvc
class UserControllerTest {

    @Mock
    private lateinit var mockMvc: MockMvc

    @Mock
    private lateinit var mockUserService: UserService

    @InjectMocks
    private lateinit var userController: UserController

    @BeforeEach
    fun setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build()
    }

    @Test
    @WithMockUser(username = "test@example.com")
    fun `GET「／auth／user」が呼ばれたとき、ステータス200が返ってくる`() {

        mockMvc.perform(get("/auth/user"))
            .andExpect(status().isOk)
    }

    @Test
    @WithMockUser(username = "test@example.com")
    fun `GET「／auth／user」が呼ばれたとき、userServiceのfindByUserEmail()が呼ばれ、指定したuserのuserIdとuserEmailが返ってくる`() {
        val mockEmail = "test@example.com"

        `when`(mockUserService.findByUserEmail(mockEmail))
            .thenReturn(AuthUserResponse(true, "認証されたユーザーです。", 1, "test@example.com"))

        mockMvc.perform(get("/auth/user"))
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.message").value("認証されたユーザーです。"))
            .andExpect(jsonPath("$.authUserId").value(1))
            .andExpect(jsonPath("$.authUserEmail").value("test@example.com"))

        verify(mockUserService, times(1)).findByUserEmail(mockEmail)
    }

    @Test
    fun `DELETE「／user／userId」が呼ばれたとき、userServiceのdeleteByUserId()が呼ばれる`() {

        mockMvc.perform(delete("/user/1"))

        verify(mockUserService, times(1)).deleteByUserId(1)
    }

    @Test
    fun `PUT「／users／userId／email」が呼ばれたとき、ステータス200が返ってくる`() {
        val mockIncludeNewEmailRequest = IncludeNewEmailRequest(1, "mockEmail@test.com", "mockPass1234")
        val mapper = ObjectMapper()
        val json = mapper.writeValueAsString(mockIncludeNewEmailRequest)

        mockMvc.perform(put("/users/1/email")
            .contentType(MediaType.APPLICATION_JSON)
            .content(json))
            .andExpect(status().isOk)
    }

    @Test
    fun `PUT「／users／userId／email」が呼ばれたとき、userServiceのupdateUserEmail()が呼ばれる` () {
        val mockIncludeNewEmailRequest = IncludeNewEmailRequest(1, "mockEmail@test.com", "mockPass1234")
        val mapper = ObjectMapper()
        val json = mapper.writeValueAsString(mockIncludeNewEmailRequest)

        mockMvc.perform(put("/users/1/email")
            .contentType(MediaType.APPLICATION_JSON)
            .content(json))
            .andExpect(jsonPath("$.message").value("メールアドレスが更新されました。"))

        verify(mockUserService, times(1)).updateUserEmail(mockIncludeNewEmailRequest)
    }

    @Test
    fun `PUT「／users／userId／password」が呼ばれたとき、ステータス200が返ってくる`() {
        val mockUpdateUserPasswordRequest = UpdateUserPasswordRequest(1, "mockCurrentPass1234", "mockNewPass1234")
        val mapper = ObjectMapper()
        val json = mapper.writeValueAsString(mockUpdateUserPasswordRequest)

        mockMvc.perform(put("/users/1/password")
            .contentType(MediaType.APPLICATION_JSON)
            .content(json))
            .andExpect(status().isOk)
    }

    @Test
    fun `PUT「／users／userId／password」が呼ばれたとき、userServiceのupdateUserPassword()が呼ばれる` () {
        val mockUpdateUserPasswordRequest = UpdateUserPasswordRequest(1, "mockCurrentPass1234", "mockNewPass1234")
        val mapper = ObjectMapper()
        val json = mapper.writeValueAsString(mockUpdateUserPasswordRequest)

        mockMvc.perform(put("/users/1/password")
            .contentType(MediaType.APPLICATION_JSON)
            .content(json))
            .andExpect(jsonPath("$.message").value("パスワードが更新されました。"))

        verify(mockUserService, times(1)).updateUserPassword(mockUpdateUserPasswordRequest)
    }
}