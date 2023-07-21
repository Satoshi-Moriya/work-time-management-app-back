package com.example.worktimemanagement.service

import com.example.worktimemanagement.entity.User
import com.example.worktimemanagement.repository.UserRepository
import org.springframework.stereotype.Service

interface UserService {

    fun save(user: User): User
}

@Service
class UserServiceImpl(val userRepository: UserRepository): UserService {

    override fun save(user: User): User {
        return userRepository.save(user)
    }
}