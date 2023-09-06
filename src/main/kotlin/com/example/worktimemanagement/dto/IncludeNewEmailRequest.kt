package com.example.worktimemanagement.dto

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull

data class IncludeNewEmailRequest (

    @field: NotNull
    val userId: Int,

    @field: [Email NotBlank]
    val email: String,

    @field: NotBlank
    val password: String
)