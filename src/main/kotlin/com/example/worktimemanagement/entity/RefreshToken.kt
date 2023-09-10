package com.example.worktimemanagement.entity

import jakarta.persistence.*
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import java.time.LocalDateTime

@Entity
@Table(name = "refresh_token")
data class RefreshToken (

    @Id
    @Column(name = "refresh_token_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val refreshTokenId: Int,

    @Column(name = "user_email", unique = true)
    @field: [Email NotBlank]
    val userEmail: String,

    @Column(name = "refresh_token")
    var refreshToken: String,

    @Column(name = "refresh_token_iat")
    var refreshTokenIssuedAt: LocalDateTime
)