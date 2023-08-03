package com.example.worktimemanagement.service

import com.example.worktimemanagement.controller.AuthUserResponse
import com.example.worktimemanagement.controller.IncludeNewEmailRequest
import com.example.worktimemanagement.entity.User
import com.example.worktimemanagement.repository.UserRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import java.util.*

@ExtendWith(MockitoExtension::class)
class UserServiceTest {

    @Mock
    private lateinit var mockUserRepository: UserRepository

    @Mock
    private lateinit var bCryptPasswordEncoder: BCryptPasswordEncoder

    @InjectMocks
    private lateinit var userService: UserServiceImpl

    @Test
    fun `findByUserEmail()が実行されると、userRepositoryのfindByUserEmail()が実行され、ユーザーが見つかった場合、「true」と「認証されたユーザーです。」userIdとが返る`() {
        `when`(mockUserRepository.findByUserEmail("test@example.com"))
            .thenReturn(Optional.of(User(1, "test@example.com", "password1234", "2023-07-01 09:00:00", null, null)))

        val expectAuthUserResponse = AuthUserResponse(true, "認証されたユーザーです。", 1 )
        assertEquals(expectAuthUserResponse, userService.findByUserEmail("test@example.com"))

        verify(mockUserRepository, times(1)).findByUserEmail("test@example.com")
    }

    @Test
    fun `findByUserEmail()が実行されると、userRepositoryのfindByUserEmail()が実行され、ユーザーが見つからなかった場合、0が返る`() {
        `when`(mockUserRepository.findByUserEmail("test@example.com"))
            .thenReturn(Optional.empty())

        val expectAuthUserResponse = AuthUserResponse(false, "認証されたユーザーではありません。", null)
        assertEquals(expectAuthUserResponse, userService.findByUserEmail("test@example.com"))

        verify(mockUserRepository, times(1)).findByUserEmail("test@example.com")
    }

    @Test
    fun `deleteByUserId()が実行されると、userRepositoryのdeleteByUserId()が実行される`() {
        userService.deleteByUserId(1)

        verify(mockUserRepository, times(1)).deleteByUserId(eq(1), anyString())
    }

    @Test
    fun `updateUserEmail()が実行されると、userRepositoryのupdateUserEmail()が実行される` () {
        val mockIncludeNewEmailRequest = IncludeNewEmailRequest(1, "mockEmail@test.com", "mockPass1234")

        `when`(bCryptPasswordEncoder.encode("mockPass1234")).thenReturn("encodedMockPass1234")

        userService.updateUserEmail(mockIncludeNewEmailRequest)

        verify(mockUserRepository, times(1)).updateUserEmail(1,"mockEmail@test.com", "encodedMockPass1234")
    }
}