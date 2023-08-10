package com.example.worktimemanagement.entity

import jakarta.persistence.*
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern
import org.hibernate.validator.constraints.Length

@Entity
@Table(name = "users")
data class User (

    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val userId: Int,

    @Column(name = "user_email", unique = true)
    @field: [Email NotBlank]
    val userEmail: String,

    @Column(name = "user_password")
    @field: [NotBlank Length(min = 8, message = "The value must contain at least 8 characters")]
    val userPassword: String,

    @Column(name = "created_at")
    @field: [NotBlank Pattern(regexp = "\\d{4}-(0[1-9]|1[0-2])-(0[1-9]|[12][0-9]|3[01]) ([01][0-9]|2[0-3]):[0-5][0-9]:[0-5][0-9]", message = "Invalid date format")]
    val createdAt: String,

    @Column(name = "updated_at")
    @field: Pattern(regexp = "\\d{4}-(0[1-9]|1[0-2])-(0[1-9]|[12][0-9]|3[01]) ([01][0-9]|2[0-3]):[0-5][0-9]:[0-5][0-9]", message = "Invalid date format")
    val updatedAt: String?,

    @Column(name = "deleted_at")
    @field: Pattern(regexp = "\\d{4}-(0[1-9]|1[0-2])-(0[1-9]|[12][0-9]|3[01]) ([01][0-9]|2[0-3]):[0-5][0-9]:[0-5][0-9]", message = "Invalid date format")
    val deletedAt: String?
)