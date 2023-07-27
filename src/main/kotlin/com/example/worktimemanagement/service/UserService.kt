package com.example.worktimemanagement.service

import com.example.worktimemanagement.controller.AuthUserResponse
import com.example.worktimemanagement.entity.User
import com.example.worktimemanagement.repository.UserRepository
import com.example.worktimemanagement.security.MyUserDetails
import org.springframework.stereotype.Service

interface UserService {

    fun save(user: User): User
    fun findByUserEmail(username: String): AuthUserResponse
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
}