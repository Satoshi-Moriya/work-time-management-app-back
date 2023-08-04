package com.example.worktimemanagement.service

import com.example.worktimemanagement.controller.*
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
    fun `findByUserEmail()が実行されると、userRepositoryのfindByUserEmail()が実行され、ユーザーが見つかった場合、「true」と「認証されたユーザーです。」とuserIdとuserEmailが返る`() {
        `when`(mockUserRepository.findByUserEmail("test@example.com"))
            .thenReturn(Optional.of(User(1, "test@example.com", "password1234", "2023-07-01 09:00:00", null, null)))

        val expectAuthUserResponse = AuthUserResponse(true, "認証されたユーザーです。", 1, "test@example.com")
        assertEquals(expectAuthUserResponse, userService.findByUserEmail("test@example.com"))

        verify(mockUserRepository, times(1)).findByUserEmail("test@example.com")
    }

    @Test
    fun `findByUserEmail()が実行されると、userRepositoryのfindByUserEmail()が実行され、ユーザーが見つからなかった場合、「false」と「認証されたユーザーではありません。」nullが返る`() {
        `when`(mockUserRepository.findByUserEmail("test@example.com"))
            .thenReturn(Optional.empty())

        val expectAuthUserResponse = AuthUserResponse(false, "認証されたユーザーではありません。", null, null)
        assertEquals(expectAuthUserResponse, userService.findByUserEmail("test@example.com"))

        verify(mockUserRepository, times(1)).findByUserEmail("test@example.com")
    }

    @Test
    fun `deleteByUserId()が実行されると、userRepositoryのdeleteByUserId()が実行される`() {
        userService.deleteByUserId(1)

        verify(mockUserRepository, times(1)).deleteByUserId(eq(1), anyString())
    }

    @Test
    fun `updateUserEmail()が実行され、現在のパスワードと入力されたパスワードが一致した時、userRepositoryのupdateUserEmail()が実行され、trueと「メールアドレスが更新されました。」が返る` () {
        val mockIncludeNewEmailRequest = IncludeNewEmailRequest(1, "mockEmail@test.com", "mockPass1234")

        `when`(mockUserRepository.getCurrentPassword(1)).thenReturn("encodedMockPass1234")
        `when`(bCryptPasswordEncoder.matches("mockPass1234", "encodedMockPass1234")).thenReturn(true)

        val expectUpdateUserEmailResponse = UpdateUserEmailResponse("メールアドレスが更新されました。")
        assertEquals(expectUpdateUserEmailResponse, userService.updateUserEmail(mockIncludeNewEmailRequest))

        verify(mockUserRepository, times(1)).updateUserEmail(1,"mockEmail@test.com")
    }

    @Test
    fun `updateUserEmail()が実行され、現在のパスワードと入力されたパスワードが一致しなかった時、falseと「パスワードが間違っており、メールアドレスの更新ができませんでした。」が返る` () {
        val mockIncludeNewEmailRequest = IncludeNewEmailRequest(1, "mockEmail@test.com", "mockPass1235")

        `when`(mockUserRepository.getCurrentPassword(1)).thenReturn("encodedMockPass1234")
        `when`(bCryptPasswordEncoder.matches("mockPass1235", "encodedMockPass1234")).thenReturn(false)

        val expectUpdateUserEmailResponse = UpdateUserEmailResponse("パスワードが間違っており、メールアドレスの更新ができませんでした。")
        assertEquals(expectUpdateUserEmailResponse, userService.updateUserEmail(mockIncludeNewEmailRequest))
    }

    @Test
    fun `updateUserEmail()が実行され、現在のパスワードが見つからない時、falseと「予期せぬ問題が起こり、メールアドレスの更新ができませんでした。」が返る` () {
        val mockIncludeNewEmailRequest = IncludeNewEmailRequest(1, "mockEmail@test.com", "mockPass1234")

        `when`(mockUserRepository.getCurrentPassword(1)).thenReturn(null)

        val expectUpdateUserEmailResponse = UpdateUserEmailResponse("予期せぬ問題が起こり、メールアドレスの更新ができませんでした。")
        assertEquals(expectUpdateUserEmailResponse, userService.updateUserEmail(mockIncludeNewEmailRequest))
    }

    @Test
    fun `updateUserPassword()が実行され、現在のパスワードと入力されたパスワードが一致した時、userRepositoryのupdateUserPassword()が実行され、「パスワードが更新されました。」が返る` () {
        val mockUpdateUserPasswordRequest = UpdateUserPasswordRequest(1, "mockCurrentPass1234", "mockNewPass1234")

        `when`(mockUserRepository.getCurrentPassword(1)).thenReturn("encodedMockPass1234")
        `when`(bCryptPasswordEncoder.matches("mockCurrentPass1234", "encodedMockPass1234")).thenReturn(true)
        `when`(bCryptPasswordEncoder.encode("mockNewPass1234")).thenReturn("mockEncodeNewPass1234")

        val expectUpdateUserPasswordResponse = UpdateUserPasswordResponse("パスワードが更新されました。")
        assertEquals(expectUpdateUserPasswordResponse, userService.updateUserPassword(mockUpdateUserPasswordRequest))

        verify(mockUserRepository, times(1)).updateUserPassword(1,"mockEncodeNewPass1234")
    }

    @Test
    fun `updateUserPassword()が実行され、現在のパスワードと入力されたパスワードが一致しなかった時、「現在のパスワードが間違っており、パスワードの更新ができませんでした。」が返る` () {
        val mockUpdateUserPasswordRequest = UpdateUserPasswordRequest(1, "mockCurrentPass1235", "mockNewPass1234")

        `when`(mockUserRepository.getCurrentPassword(1)).thenReturn("encodedMockPass1234")
        `when`(bCryptPasswordEncoder.matches("mockCurrentPass1235", "encodedMockPass1234")).thenReturn(false)

        val expectUpdateUserPasswordResponse = UpdateUserPasswordResponse("現在のパスワードが間違っており、パスワードの更新ができませんでした。")
        assertEquals(expectUpdateUserPasswordResponse, userService.updateUserPassword(mockUpdateUserPasswordRequest))
    }

    @Test
    fun `updateUserPassword()が実行され、現在のパスワードが見つからない時、「予期せぬ問題が起こり、パスワードの更新ができませんでした。」が返る` () {
        val mockUpdateUserPasswordRequest = UpdateUserPasswordRequest(1, "mockCurrentPass1234", "mockNewPass1234")

        `when`(mockUserRepository.getCurrentPassword(1)).thenReturn(null)

        val expectUpdateUserPasswordResponse = UpdateUserPasswordResponse("予期せぬ問題が起こり、パスワードの更新ができませんでした。")
        assertEquals(expectUpdateUserPasswordResponse, userService.updateUserPassword(mockUpdateUserPasswordRequest))
    }
}