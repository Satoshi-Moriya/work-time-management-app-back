package com.example.worktimemanagement.dto

data class AuthUserResponse (
    val success: Boolean,
    val message: String,
    val authUserId: Int?,
    val authUserEmail: String?
)