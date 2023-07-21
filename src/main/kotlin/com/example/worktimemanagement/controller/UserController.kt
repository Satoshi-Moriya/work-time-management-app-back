package com.example.worktimemanagement.controller

import com.example.worktimemanagement.entity.User
import com.example.worktimemanagement.service.UserService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@CrossOrigin
class UserController(val userService: UserService) {

    // テストリクエスト
    // curl --location --request POST 'http://localhost:8080/auth/signup' \
    // --header 'Content-Type: application/json' \
    // --data-raw '{"userMainAddress" : "ユニークなメールアドレスを設定", "userPassword" : "test5678", "createdAt" : "2023-07-03 12:00:00", "updatedAt" : null, "deletedAt" : null}' | jq
    @PostMapping("/auth/signup")
    @ResponseStatus(HttpStatus.CREATED)
    fun register(@RequestBody userBody: User): User {
        return userService.save(userBody)
    }
}