package com.example.worktimemanagement.controller

import com.example.worktimemanagement.entity.User
import com.example.worktimemanagement.service.UserService
import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.validation.ConstraintViolationException
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.*
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.http.MediaType
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.MvcResult
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
    fun `UserのuserEmail不正な値が入っている場合、ステータス400が返ってくる（POST「／auth／signup」を利用して確認）`() {
        val mockUserRequest = User(1, "Invalid-Email", "mockPass1235", "2023-07-01 09:00:00", null, null)
        val mapper = ObjectMapper()
        val json = mapper.writeValueAsString(mockUserRequest)

        mockMvc.perform(post("/auth/signup")
            .contentType(MediaType.APPLICATION_JSON)
            .content(json))
            .andExpect(status().isBadRequest)
    }

    @Test
    fun `UserのuserEmailに空文字が入っている場合、ステータス400が返ってくる（POST「／auth／signup」を利用して確認）`() {
        val mockUserRequest = User(1, "", "mockPass1235", "2023-07-01 09:00:00", null, null)
        val mapper = ObjectMapper()
        val json = mapper.writeValueAsString(mockUserRequest)

        mockMvc.perform(post("/auth/signup")
            .contentType(MediaType.APPLICATION_JSON)
            .content(json))
            .andExpect(status().isBadRequest)
    }

    @Test
    fun `UserのuserPasswordが空文字の場合、ステータス400が返ってくる（POST「／auth／signup」を利用して確認）`() {
        val mockUserRequest = User(1, "mock@test.com", "", "2023-07-01 09:00:00", null, null)
        val mapper = ObjectMapper()
        val json = mapper.writeValueAsString(mockUserRequest)

        mockMvc.perform(post("/auth/signup")
            .contentType(MediaType.APPLICATION_JSON)
            .content(json))
            .andExpect(status().isBadRequest)
    }

    @Test
    fun `UserのuserPasswordが8文字未満の場合、ステータス400が返ってくる（POST「／auth／signup」を利用して確認）`() {
        val mockUserRequest = User(1, "mock@test.com", "test123", "2023-07-01 09:00:00", null, null)
        val mapper = ObjectMapper()
        val json = mapper.writeValueAsString(mockUserRequest)

        mockMvc.perform(post("/auth/signup")
            .contentType(MediaType.APPLICATION_JSON)
            .content(json))
            .andExpect(status().isBadRequest)
    }

    @Test
    fun `UserのcreatedAtの年のフォーマットが崩れていた場合、ステータス400が返ってくる（POST「／auth／signup」を利用して確認）`() {
        val mockUserRequest = User(1, "mock@test.com", "mockTest1234", "223-09-31 03:59:59", null, null)
        val mapper = ObjectMapper()
        val json = mapper.writeValueAsString(mockUserRequest)

        mockMvc.perform(post("/auth/signup")
            .contentType(MediaType.APPLICATION_JSON)
            .content(json))
            .andExpect(status().isBadRequest)
    }

    @Test
    fun `UserのcreatedAtの月が13の場合、ステータス400が返ってくる（POST「／auth／signup」を利用して確認）`() {
        val mockUserRequest = User(1, "mock@test.com", "mockTest1234", "2023-13-01 09:00:00", null, null)
        val mapper = ObjectMapper()
        val json = mapper.writeValueAsString(mockUserRequest)

        mockMvc.perform(post("/auth/signup")
            .contentType(MediaType.APPLICATION_JSON)
            .content(json))
            .andExpect(status().isBadRequest)
    }

    @Test
    fun `UserのcreatedAtの月のフォーマットが崩れていた場合、ステータス400が返ってくる（POST「／auth／signup」を利用して確認）`() {
        val mockUserRequest = User(1, "mock@test.com", "mockTest1234", "2023-9-31 03:59:59", null, null)
        val mapper = ObjectMapper()
        val json = mapper.writeValueAsString(mockUserRequest)

        mockMvc.perform(post("/auth/signup")
            .contentType(MediaType.APPLICATION_JSON)
            .content(json))
            .andExpect(status().isBadRequest)
    }

    @Test
    fun `UserのcreatedAtの日が32の場合、ステータス400が返ってくる（POST「／auth／signup」を利用して確認）`() {
        val mockUserRequest = User(1, "mock@test.com", "mockTest1234", "2023-12-32 09:00:00", null, null)
        val mapper = ObjectMapper()
        val json = mapper.writeValueAsString(mockUserRequest)

        mockMvc.perform(post("/auth/signup")
            .contentType(MediaType.APPLICATION_JSON)
            .content(json))
            .andExpect(status().isBadRequest)
    }

    @Test
    fun `UserのcreatedAtの日のフォーマットが崩れていた場合、ステータス400が返ってくる（POST「／auth／signup」を利用して確認）`() {
        val mockUserRequest = User(1, "mock@test.com", "mockTest1234", "2023-12-1 03:59:59", null, null)
        val mapper = ObjectMapper()
        val json = mapper.writeValueAsString(mockUserRequest)

        mockMvc.perform(post("/auth/signup")
            .contentType(MediaType.APPLICATION_JSON)
            .content(json))
            .andExpect(status().isBadRequest)
    }

    @Test
    fun `UserのcreatedAtの時間が24の場合、ステータス400が返ってくる（POST「／auth／signup」を利用して確認）`() {
        val mockUserRequest = User(1, "mock@test.com", "mockTest1234", "2023-12-31 24:00:00", null, null)
        val mapper = ObjectMapper()
        val json = mapper.writeValueAsString(mockUserRequest)

        mockMvc.perform(post("/auth/signup")
            .contentType(MediaType.APPLICATION_JSON)
            .content(json))
            .andExpect(status().isBadRequest)
    }

    @Test
    fun `UserのcreatedAtの時間のフォーマットが崩れていた場合、ステータス400が返ってくる（POST「／auth／signup」を利用して確認）`() {
        val mockUserRequest = User(1, "mock@test.com", "mockTest1234", "2023-12-01 3:59:59", null, null)
        val mapper = ObjectMapper()
        val json = mapper.writeValueAsString(mockUserRequest)

        mockMvc.perform(post("/auth/signup")
            .contentType(MediaType.APPLICATION_JSON)
            .content(json))
            .andExpect(status().isBadRequest)
    }

    @Test
    fun `UserのcreatedAtの分が60の場合、ステータス400が返ってくる（POST「／auth／signup」を利用して確認）`() {
        val mockUserRequest = User(1, "mock@test.com", "mockTest1234", "2023-12-31 23:60:00", null, null)
        val mapper = ObjectMapper()
        val json = mapper.writeValueAsString(mockUserRequest)

        mockMvc.perform(post("/auth/signup")
            .contentType(MediaType.APPLICATION_JSON)
            .content(json))
            .andExpect(status().isBadRequest)
    }

    @Test
    fun `UserのcreatedAtの分のフォーマットが崩れていた場合、ステータス400が返ってくる（POST「／auth／signup」を利用して確認）`() {
        val mockUserRequest = User(1, "mock@test.com", "mockTest1234", "2023-12-01 03:9:59", null, null)
        val mapper = ObjectMapper()
        val json = mapper.writeValueAsString(mockUserRequest)

        mockMvc.perform(post("/auth/signup")
            .contentType(MediaType.APPLICATION_JSON)
            .content(json))
            .andExpect(status().isBadRequest)
    }

    @Test
    fun `UserのcreatedAtの秒が60の場合、ステータス400が返ってくる（POST「／auth／signup」を利用して確認）`() {
        val mockUserRequest = User(1, "mock@test.com", "mockTest1234", "2023-12-31 23:59:60", null, null)
        val mapper = ObjectMapper()
        val json = mapper.writeValueAsString(mockUserRequest)

        mockMvc.perform(post("/auth/signup")
            .contentType(MediaType.APPLICATION_JSON)
            .content(json))
            .andExpect(status().isBadRequest)
    }

    @Test
    fun `UserのcreatedAtの秒のフォーマットが崩れていた場合、ステータス400が返ってくる（POST「／auth／signup」を利用して確認）`() {
        val mockUserRequest = User(1, "mock@test.com", "mockTest1234", "2023-12-01 03:59:9", null, null)
        val mapper = ObjectMapper()
        val json = mapper.writeValueAsString(mockUserRequest)

        mockMvc.perform(post("/auth/signup")
            .contentType(MediaType.APPLICATION_JSON)
            .content(json))
            .andExpect(status().isBadRequest)
    }

    @Test
    fun `UserのcreatedAtの年月日と時間の間のスペースがない場合、ステータス400が返ってくる（POST「／auth／signup」を利用して確認）`() {
        val mockUserRequest = User(1, "mock@test.com", "mockTest1234", "2023-12-0103:59:09", null, null)
        val mapper = ObjectMapper()
        val json = mapper.writeValueAsString(mockUserRequest)

        mockMvc.perform(post("/auth/signup")
            .contentType(MediaType.APPLICATION_JSON)
            .content(json))
            .andExpect(status().isBadRequest)
    }

    @Test
    fun `POST「／auth／signup」が正常に呼ばれたとき、ステータス201が返ってくる`() {
        val mockUserRequest = User(1, "mockEmail@test.com", "mockPass1235", "2023-07-01 09:00:00", null, null)
        val mapper = ObjectMapper()
        val json = mapper.writeValueAsString(mockUserRequest)

        mockMvc.perform(post("/auth/signup")
            .contentType(MediaType.APPLICATION_JSON)
            .content(json))
            .andExpect(status().isCreated)
    }

    @Test
    fun `POST「／auth／signup」が正常に呼ばれたとき、userServiceのsave()が呼ばれる`() {
        val mockUserRequest = User(1, "mockEmail@test.com", "mockPass1235", "2023-07-01 09:00:00", null, null)
        val mapper = ObjectMapper()
        val json = mapper.writeValueAsString(mockUserRequest)

        mockMvc.perform(post("/auth/signup")
            .contentType(MediaType.APPLICATION_JSON)
            .content(json))

        verify(mockUserService, times(1)).save(mockUserRequest)
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
    fun `DELETE「／users／{userId}」が呼ばれたとき、userServiceのdeleteByUserId()が呼ばれる`() {

        mockMvc.perform(delete("/users/1"))

        verify(mockUserService, times(1)).deleteByUserId(1)
    }

    @Test
    fun `GET「／users／userId／email」が正常に呼ばれたとき、ステータス200が返ってくる`() {
        mockMvc.perform(get("/users/1/email"))
            .andExpect(status().isOk)
    }

    @Test
    fun `PUT「／users／userId／email」が正常に呼ばれたとき、ステータス200が返ってくる`() {
        val mockIncludeNewEmailRequest = IncludeNewEmailRequest(1, "mockEmail@test.com", "mockPass1234")
        val mapper = ObjectMapper()
        val json = mapper.writeValueAsString(mockIncludeNewEmailRequest)

        mockMvc.perform(put("/users/1/email")
            .contentType(MediaType.APPLICATION_JSON)
            .content(json))
            .andExpect(status().isOk)
    }

    @Test
    fun `PUT「／users／userId／email」のリクエストのemailが不正な値の場合、ステータス400が返ってくる`() {
        val mockIncludeNewEmailRequest = IncludeNewEmailRequest(1, "mockEmailtest.com", "mockPass1234")
        val mapper = ObjectMapper()
        val json = mapper.writeValueAsString(mockIncludeNewEmailRequest)

        mockMvc.perform(put("/users/1/email")
            .contentType(MediaType.APPLICATION_JSON)
            .content(json))
            .andExpect(status().isBadRequest)
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