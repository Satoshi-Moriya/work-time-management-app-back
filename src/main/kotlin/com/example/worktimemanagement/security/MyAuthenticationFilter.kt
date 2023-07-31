package com.example.worktimemanagement.security

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.ObjectMapper
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import jakarta.servlet.FilterChain
import jakarta.servlet.ServletException
import jakarta.servlet.http.Cookie
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpCookie
import org.springframework.http.HttpHeaders
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.security.web.util.matcher.AntPathRequestMatcher
import java.io.IOException
import java.util.*


class MyAuthenticationFilter(
    private var authenticationManager: AuthenticationManager,
    private var bCryptPasswordEncoder: BCryptPasswordEncoder
): UsernamePasswordAuthenticationFilter() {
    companion object {
        private val secretKey = System.getenv("MY_SECRET_KEY")
        private val key by lazy { Keys.hmacShaKeyFor(secretKey.toByteArray()) }
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
        val email = auth.principal as UserDetails
        val token: String = Jwts.builder()
            .setSubject(email.username)
            .setExpiration(Date(System.currentTimeMillis() + 30 * 60 * 1000))
            .signWith(key)
            .compact()

        val user = auth.principal as MyUserDetails
        res.writer.write(ObjectMapper().writeValueAsString(user.user.copy(userPassword = "")))

        res.addHeader(HttpHeaders.AUTHORIZATION, "Bearer $token")
        val cookie = Cookie("token", token)
            cookie.path = "/"
            cookie.isHttpOnly = true
//        ToDo httpsの通信のみ有効にするっぽいので本番では適応させる
//          cookie.secure = true
        res.addCookie(cookie)
    }
}

data class AuthRequest @JsonCreator constructor (
    @JsonProperty("userEmail") val userEmail: String,
    @JsonProperty("userPassword") val userPassword: String
)