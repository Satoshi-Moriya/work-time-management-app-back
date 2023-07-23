package com.example.worktimemanagement.security

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.ObjectMapper
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import jakarta.servlet.FilterChain
import jakarta.servlet.ServletException
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpHeaders
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
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

        res.addHeader(HttpHeaders.AUTHORIZATION, "Bearer $token")
    }
}

data class AuthRequest @JsonCreator constructor (
    @JsonProperty("userEmail") val userEmail: String,
    @JsonProperty("userPassword") val userPassword: String
)