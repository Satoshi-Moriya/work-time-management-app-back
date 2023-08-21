package com.example.worktimemanagement.security

import com.example.worktimemanagement.dto.AuthRequest
import com.example.worktimemanagement.dto.AuthResponse
import com.example.worktimemanagement.dto.UserIssueToken
import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.servlet.FilterChain
import jakarta.servlet.ServletException
import jakarta.servlet.http.Cookie
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.security.web.util.matcher.AntPathRequestMatcher
import java.io.IOException


class MyAuthenticationFilter(
    private var authenticationManager: AuthenticationManager,
    private var bCryptPasswordEncoder: BCryptPasswordEncoder,
    private val myUserDetailsService: MyUserDetailsService
): UsernamePasswordAuthenticationFilter() {

    companion object {
        private val accessTokenSecret = System.getenv("MY_SECRET_KEY")
    }

    init {
        this.authenticationManager = authenticationManager
        this.bCryptPasswordEncoder = bCryptPasswordEncoder
        setRequiresAuthenticationRequestMatcher(AntPathRequestMatcher("/login", "POST"))

        usernameParameter = "userEmail"
        usernameParameter =  "userPassword"
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

    @Throws(IOException::class, ServletException::class)
    override fun successfulAuthentication(
        req: HttpServletRequest?,
        res: HttpServletResponse,
        chain: FilterChain?,
        auth: Authentication
    ) {
        val user = auth.principal as MyUserDetails
        val token: UserIssueToken = myUserDetailsService.issueToken(user.user.userEmail)
        val accessToken: String = token.accessToken
        val refreshToken: String = token.refreshToken

        res.writer.write(ObjectMapper().writeValueAsString(
            AuthResponse(
                user.user.userId,
                user.user.userEmail
            )
        ))
        val accessTokenCookie = createCookie("accessToken", accessToken)
        val refreshTokenCookie = createCookie("refreshToken", refreshToken)
//        ToDo httpsの通信のみ有効にするっぽいので本番では適応させる
//          cookie.secure = true
        res.addCookie(accessTokenCookie)
        res.addCookie(refreshTokenCookie)
    }

    private fun createCookie(name: String, value: String): Cookie {
        return Cookie(name, value).apply {
            path = "/"
            isHttpOnly = true
        }
    }
}