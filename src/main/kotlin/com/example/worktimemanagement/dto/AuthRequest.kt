package com.example.worktimemanagement.dto

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty

data class AuthRequest @JsonCreator constructor (
    @JsonProperty("userEmail") val userEmail: String,
    @JsonProperty("userPassword") val userPassword: String
)