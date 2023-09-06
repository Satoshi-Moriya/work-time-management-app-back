package com.example.worktimemanagement.service

import com.example.worktimemanagement.controller.*
import com.example.worktimemanagement.entity.User
import com.example.worktimemanagement.error.InvalidPasswordException
import com.example.worktimemanagement.repository.UserRepository
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

interface UserService {

    fun save(user: User)

    fun findByUserEmail(username: String): AuthUserResponse

    fun deleteByUserId(userId: Int)

    fun updateUserEmail(includeNewEmailRequest: IncludeNewEmailRequest)
    
    fun updateUserPassword(request: UpdateUserPasswordRequest)
    fun fetchUserEmail(userId: Int): String
}

@Service
class UserServiceImpl(
    val userRepository: UserRepository,
    val bCryptPasswordEncoder: BCryptPasswordEncoder
): UserService {

    override fun save(user: User) {
        val hashPassUser = User(
            user.userId,
            user.userEmail,
            bCryptPasswordEncoder.encode(user.userPassword),
            user.createdAt,
            null,
            null
        )
        userRepository.save(hashPassUser)
    }

    override fun findByUserEmail(userEmail: String): AuthUserResponse {
        return userRepository.findByUserEmail(userEmail)?.let {
            user ->
                AuthUserResponse(
                    true,
                    "認証されたユーザーです。",
                    user.userId,
                    user.userEmail
                )
            } ?: AuthUserResponse(
                    false,
                    "認証されたユーザーではありません。",
                    null,
                    null
                )
    }

    override fun deleteByUserId(userId: Int) {
        val deletedAt = getCurrentDateTimeAsString()
        userRepository.deleteByUserId(userId, deletedAt)
    }

    override fun fetchUserEmail(userId: Int): String {
        return userRepository.findByUserId(userId).userEmail
    }

    override fun updateUserEmail(includeNewEmailRequest: IncludeNewEmailRequest) {
        return userRepository.fetchPassword(includeNewEmailRequest.userId)
            .let { userCurrentPassword ->
                if (bCryptPasswordEncoder.matches(includeNewEmailRequest.password, userCurrentPassword)) {
                    userRepository.updateUserEmail(
                        includeNewEmailRequest.userId,
                        includeNewEmailRequest.email
                    )
                } else {
                    throw InvalidPasswordException("無効なパスワードです。")
                }
            }
    }

    override fun updateUserPassword(updateUserPasswordRequest: UpdateUserPasswordRequest) {
        return userRepository.fetchPassword(updateUserPasswordRequest.userId)
            .let { userCurrentPassword ->
                if (bCryptPasswordEncoder.matches(updateUserPasswordRequest.currentPassword, userCurrentPassword)) {
                    userRepository.updateUserPassword(
                        updateUserPasswordRequest.userId,
                        bCryptPasswordEncoder.encode(updateUserPasswordRequest.newPassword)
                    )
                } else {
                    throw InvalidPasswordException("無効なパスワードです。")
                }
            }
    }

    private fun getCurrentDateTimeAsString(): String {
        val currentDateTime = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        return currentDateTime.format(formatter)
    }

}