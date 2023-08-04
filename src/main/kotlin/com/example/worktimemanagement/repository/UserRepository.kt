package com.example.worktimemanagement.repository

import com.example.worktimemanagement.entity.User
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.util.Optional

@Transactional
@Repository
interface UserRepository : CrudRepository<User, Int> {

    @Query("SELECT u FROM User u WHERE u.userEmail = :email AND u.deletedAt IS NULL")
    fun findByUserEmail(email: String): Optional<User>

    @Modifying
    @Query("UPDATE User u SET u.deletedAt = :deletedAt WHERE u.userId = :userId")
    fun deleteByUserId(userId: Int, deletedAt: String)

    @Query("SELECT u.userPassword FROM User u WHERE u.userId = :userId")
    fun getCurrentPassword(userId: Int): String?

    @Modifying
    @Query("UPDATE User u SET u.userEmail = :userEmail WHERE u.userId = :userId")
    fun updateUserEmail(userId: Int, userEmail: String)

    @Modifying
    @Query("UPDATE User u SET u.userPassword = :newPassword WHERE u.userId = :userId")
    fun updateUserPassword(userId: Int, newPassword: String) {

    }
}