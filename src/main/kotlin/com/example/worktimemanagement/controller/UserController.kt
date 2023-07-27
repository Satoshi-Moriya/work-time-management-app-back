package com.example.worktimemanagement.controller

import com.example.worktimemanagement.entity.User
import com.example.worktimemanagement.service.UserService
import org.springframework.http.HttpStatus
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.*


@RestController
@CrossOrigin
class UserController(val userService: UserService) {

    // テストリクエスト
    // curl --location --request POST 'http://localhost:8080/auth/signup' \
    // --header 'Content-Type: application/json' \
    // --data-raw '{"userEmail" : "ユニークなメールアドレスを設定", "userPassword" : "test5678", "createdAt" : "2023-07-03 12:00:00", "updatedAt" : null, "deletedAt" : null}' | jq
    @PostMapping("/auth/signup")
    @ResponseStatus(HttpStatus.CREATED)
    fun register(@RequestBody userBody: User): User {
        return userService.save(userBody)
    }

    @GetMapping("/auth/user")
    fun getAuthUser(): AuthUserResponse {
        val authentication: Authentication = SecurityContextHolder.getContext().authentication

        val username = authentication.name
        return userService.findByUserEmail(username)
    }
}

data class AuthUserResponse (
    val success: Boolean,
    val message: String,
    val authUserId: Int?
)