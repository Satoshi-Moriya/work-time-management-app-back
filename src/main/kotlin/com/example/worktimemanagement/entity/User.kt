package com.example.worktimemanagement.entity

import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.*

@Entity
@Table(name = "users")
data class User (

    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val userId: Int,

    @Column(name = "user_mail_address")
    val userMainAddress: String,

    @Column(name = "user_password")
    @JsonIgnore
    val userPassword: String,

    @Column(name = "created_at")
    val createdAt: String,

    @Column(name = "updated_at")
    val updatedAt: String?,

    @Column(name = "deleted_at")
    val deletedAt: String?
)