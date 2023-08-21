package com.example.worktimemanagement.repository

import com.example.worktimemanagement.entity.RefreshToken
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface RefreshTokenRepository : CrudRepository<RefreshToken, Int> {

    fun findByUserEmail(userEmail: String): RefreshToken?
}