package com.example.worktimemanagement.service

import com.example.worktimemanagement.controller.AuthUserResponse
import com.example.worktimemanagement.entity.User
import com.example.worktimemanagement.repository.UserRepository
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

interface UserService {

    fun save(user: User): User
    fun findByUserEmail(username: String): AuthUserResponse
    fun deleteByUserId(userId: Int)
}

@Service
class UserServiceImpl(val userRepository: UserRepository): UserService {

    override fun save(user: User): User {
        return userRepository.save(user)
    }

    override fun findByUserEmail(userEmail: String): AuthUserResponse {
        return userRepository.findByUserEmail(userEmail)
            .map { user ->
                AuthUserResponse(
                    success = true,
                    message = "認証されたユーザーです。",
                    authUserId = user.userId
                )
            }
            .orElse(
                AuthUserResponse(
                    success = false,
                    message = "認証されたユーザーではありません。",
                    authUserId = null
            ))
    }

    override fun deleteByUserId(userId: Int) {
        val deletedAt = getCurrentDateTimeAsString()
        userRepository.deleteByUserId(userId, deletedAt)
    }

    private fun getCurrentDateTimeAsString(): String {
        val currentDateTime = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        return currentDateTime.format(formatter)
    }
}