package com.example.worktimemanagement.dto

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty

data class AuthResponse @JsonCreator constructor (
    @JsonProperty("userId") val userId: Int,
    @JsonProperty("userEmail") val userEmail: String
)