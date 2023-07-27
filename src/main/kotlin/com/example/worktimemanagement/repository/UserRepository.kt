package com.example.worktimemanagement.repository

import com.example.worktimemanagement.entity.User
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import java.util.Optional

interface UserRepository : CrudRepository<User, Int> {
    @Query("SELECT u FROM User u WHERE u.userEmail = :email AND u.deletedAt IS NULL")
    fun findByUserEmail(email: String): Optional<User>
}