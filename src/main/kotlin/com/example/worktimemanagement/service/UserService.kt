package com.example.worktimemanagement.service

import com.example.worktimemanagement.controller.*
import com.example.worktimemanagement.entity.User
import com.example.worktimemanagement.repository.UserRepository
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

interface UserService {

    fun save(user: User): User

    fun findByUserEmail(username: String): AuthUserResponse

    fun deleteByUserId(userId: Int)

    fun updateUserEmail(includeNewEmailRequest: IncludeNewEmailRequest): UpdateUserEmailResponse
    
    fun updateUserPassword(request: UpdateUserPasswordRequest): UpdateUserPasswordResponse
}

@Service
class UserServiceImpl(
    val userRepository: UserRepository,
    val bCryptPasswordEncoder: BCryptPasswordEncoder
): UserService {

    override fun save(user: User): User {
        return userRepository.save(user)
    }

    override fun findByUserEmail(userEmail: String): AuthUserResponse {
        return userRepository.findByUserEmail(userEmail)
            .map { user ->
                AuthUserResponse(
                    true,
                    "認証されたユーザーです。",
                    user.userId,
                    user.userEmail
                )
            }
            .orElse(
                AuthUserResponse(
                    false,
                    "認証されたユーザーではありません。",
                    null,
                    null
            ))
    }

    override fun deleteByUserId(userId: Int) {
        val deletedAt = getCurrentDateTimeAsString()
        userRepository.deleteByUserId(userId, deletedAt)
    }

    override fun updateUserEmail(includeNewEmailRequest: IncludeNewEmailRequest): UpdateUserEmailResponse {
        return userRepository.getCurrentPassword(includeNewEmailRequest.userId)
            ?.let { userCurrentPassword ->
                if (bCryptPasswordEncoder.matches(includeNewEmailRequest.password, userCurrentPassword)) {
                    userRepository.updateUserEmail(
                        includeNewEmailRequest.userId,
                        includeNewEmailRequest.email
                    )
                    UpdateUserEmailResponse("メールアドレスが更新されました。")
                } else {
                    UpdateUserEmailResponse("パスワードが間違っており、メールアドレスの更新ができませんでした。")
                }
            } ?: (
                UpdateUserEmailResponse("予期せぬ問題が起こり、メールアドレスの更新ができませんでした。")
            )
    }

    override fun updateUserPassword(updateUserPasswordRequest: UpdateUserPasswordRequest): UpdateUserPasswordResponse {
        return userRepository.getCurrentPassword(updateUserPasswordRequest.userId)
            ?.let { userCurrentPassword ->
                if (bCryptPasswordEncoder.matches(updateUserPasswordRequest.currentPassword, userCurrentPassword)) {
                    userRepository.updateUserPassword(
                        updateUserPasswordRequest.userId,
                        bCryptPasswordEncoder.encode(updateUserPasswordRequest.newPassword)
                    )
                    UpdateUserPasswordResponse("パスワードが更新されました。")
                } else {
                    UpdateUserPasswordResponse("現在のパスワードが間違っており、パスワードの更新ができませんでした。")
                }
            } ?: (
                UpdateUserPasswordResponse("予期せぬ問題が起こり、パスワードの更新ができませんでした。")
            )
    }

    private fun getCurrentDateTimeAsString(): String {
        val currentDateTime = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        return currentDateTime.format(formatter)
    }
}

