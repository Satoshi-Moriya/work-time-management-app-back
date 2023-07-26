package com.example.worktimemanagement.security

import com.example.worktimemanagement.entity.User
import java.util.*
import org.springframework.security.core.userdetails.User as MyUser

class MyUserDetails: MyUser {
    val user: User

    constructor(user: User): super(user.userEmail, user.userPassword, Collections.emptyList()) {
        this.user = user
    }
}