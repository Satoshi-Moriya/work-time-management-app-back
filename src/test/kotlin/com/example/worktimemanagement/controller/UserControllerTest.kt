package com.example.worktimemanagement.controller

import com.example.worktimemanagement.service.UserService
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
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
    fun `GET「／auth／user」が呼ばれたとき、userServiceのfindByUserEmail()が呼ばれ、指定したuserのuserIdが返ってくる`() {
        val mockEmail = "test@example.com"

        `when`(mockUserService.findByUserEmail(mockEmail))
            .thenReturn(AuthUserResponse(true, "認証されたユーザーです。", 1))

        mockMvc.perform(get("/auth/user"))
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.message").value("認証されたユーザーです。"))
            .andExpect(jsonPath("$.authUserId").value(1))

        verify(mockUserService, times(1)).findByUserEmail(mockEmail)
    }
}