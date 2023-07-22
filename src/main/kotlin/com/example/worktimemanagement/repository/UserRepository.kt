package com.example.worktimemanagement.repository

import com.example.worktimemanagement.entity.User
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository

interface UserRepository : CrudRepository<User, Int> {
    @Query("SELECT u FROM User u WHERE u.userEmail = :email")
    fun findByUserEmail(email: String): User
}