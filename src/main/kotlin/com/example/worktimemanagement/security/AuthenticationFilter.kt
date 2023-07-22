package com.example.worktimemanagement.security

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.security.web.util.matcher.AntPathRequestMatcher
import java.io.IOException


class AuthenticationFilter(
    private var authenticationManager: AuthenticationManager
): UsernamePasswordAuthenticationFilter() {

    init {
        this.authenticationManager = authenticationManager
        setRequiresAuthenticationRequestMatcher(AntPathRequestMatcher("/login", "POST"))

        setAuthenticationSuccessHandler { req: HttpServletRequest?, res: HttpServletResponse, ex: Authentication? ->
            res.status = 200
            val user: MyUserDetails =
                SecurityContextHolder.getContext().authentication.principal as MyUserDetails
            res.writer
                .write(ObjectMapper().writeValueAsString(user.username))
        }
    }

    override fun attemptAuthentication(request: HttpServletRequest, response: HttpServletResponse?): Authentication {
        try {
            val authRequest = ObjectMapper().readValue(request.inputStream, AuthRequest::class.java)
            return this.authenticationManager.authenticate(
                UsernamePasswordAuthenticationToken(
                    authRequest.userEmail,
                    authRequest.userPassword,
                    emptyList()
                )
            )
        } catch (e: IOException) {
            throw RuntimeException(e)
        }
    }
}

data class AuthRequest @JsonCreator constructor (
    @JsonProperty("userEmail") val userEmail: String,
    @JsonProperty("userPassword") val userPassword: String
)