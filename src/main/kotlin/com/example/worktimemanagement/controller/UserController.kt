package com.example.worktimemanagement.controller

import com.example.worktimemanagement.dto.AuthUserResponse
import com.example.worktimemanagement.dto.CustomResponse
import com.example.worktimemanagement.dto.IncludeNewEmailRequest
import com.example.worktimemanagement.dto.UpdateUserPasswordRequest
import com.example.worktimemanagement.entity.User
import com.example.worktimemanagement.service.UserService
import jakarta.validation.constraints.NotNull
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*

@RestController
@CrossOrigin
class UserController(val userService: UserService) {

    @PostMapping("/auth/signup")
    @ResponseStatus(HttpStatus.OK)
    fun register(@RequestBody @Validated user: User): ResponseEntity<CustomResponse> {
        userService.save(user)
        return ResponseEntity.ok(CustomResponse("アカウント登録が完了しました。"))
    }

    @GetMapping("/auth/user")
    fun getAuthUser(): AuthUserResponse {
        val authentication: Authentication = SecurityContextHolder.getContext().authentication

        val username = authentication.name
        return userService.findByUserEmail(username)
    }

    @DeleteMapping("/users/{userId}")
    @ResponseStatus(HttpStatus.OK)
    fun deleteUser(@PathVariable @NotNull userId: Int): ResponseEntity<CustomResponse> {
        userService.deleteByUserId(userId)
        return ResponseEntity.ok(CustomResponse("アカウント削除が成功しました。"))
    }

    @GetMapping("/users/{userId}/email")
    @ResponseStatus(HttpStatus.OK)
    fun fetchUserEmail(@PathVariable @NotNull userId: Int): String {
        return userService.fetchUserEmail(userId)
    }

    @PutMapping("/users/{userId}/email")
    @ResponseStatus(HttpStatus.OK)
    fun updateUserEmail(@RequestBody @Validated request: IncludeNewEmailRequest): ResponseEntity<CustomResponse> {
        userService.updateUserEmail(request)
        return ResponseEntity.ok(CustomResponse("メールアドレスが更新されました。"))
    }

    @PutMapping("/users/{userId}/password")
    @ResponseStatus(HttpStatus.OK)
    fun updateUserPassword(@RequestBody @Validated request: UpdateUserPasswordRequest): ResponseEntity<CustomResponse> {
        userService.updateUserPassword(request)
        return ResponseEntity.ok(CustomResponse("パスワードが更新されました。"))
    }
}
