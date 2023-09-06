package com.example.worktimemanagement.dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull

data class UpdateUserPasswordRequest (

    @field: NotNull
    val userId: Int,

    @field: NotBlank
    val currentPassword: String,

    @field: NotBlank
    val newPassword: String
)