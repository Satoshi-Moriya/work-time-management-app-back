package com.example.worktimemanagement.security

import com.example.worktimemanagement.repository.UserRepository
import org.springframework.stereotype.Service

@Service
class MyControllerAuth(val userRepository: UserRepository) {

    // ToDo DBの設計がそもそも悪い気がしてきたが、一旦これで回避
    fun isLoginUserId(email: String): Int? {
        return userRepository.findByUserEmail(email)?.userId
    }
}