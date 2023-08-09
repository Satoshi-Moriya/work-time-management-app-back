package com.example.worktimemanagement.controller

import com.example.worktimemanagement.dto.CustomResponse
import com.example.worktimemanagement.entity.User
import com.example.worktimemanagement.service.UserService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.*


@RestController
@CrossOrigin
class UserController(val userService: UserService) {

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

    @DeleteMapping("/users/{userId}")
    @ResponseStatus(HttpStatus.OK)
    fun deleteUser(@PathVariable userId: Int): ResponseEntity<CustomResponse> {
        userService.deleteByUserId(userId)
        return ResponseEntity.ok(CustomResponse("アカウント削除が成功しました。"))
    }

    @PutMapping("/users/{userId}/email")
    @ResponseStatus(HttpStatus.OK)
    fun updateUserEmail(@RequestBody request: IncludeNewEmailRequest): ResponseEntity<CustomResponse> {
        userService.updateUserEmail(request)
        return ResponseEntity.ok(CustomResponse("メールアドレスが更新されました。"))
    }

    @PutMapping("/users/{userId}/password")
    @ResponseStatus(HttpStatus.OK)
    fun updateUserPassword(@RequestBody request: UpdateUserPasswordRequest): ResponseEntity<CustomResponse> {
        userService.updateUserPassword(request)
        return ResponseEntity.ok(CustomResponse("パスワードが更新されました。"))
    }
}

data class AuthUserResponse (
    val success: Boolean,
    val message: String,
    val authUserId: Int?,
    val authUserEmail: String?
)

data class IncludeNewEmailRequest (
    val userId: Int,
    val email: String,
    val password: String
)

data class UpdateUserPasswordRequest (
    val userId: Int,
    val currentPassword: String,
    val newPassword: String
)
