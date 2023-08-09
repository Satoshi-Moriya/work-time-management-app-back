package com.example.worktimemanagement.security

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter

class MyAuthorizationFilter(authenticationManager: AuthenticationManager): BasicAuthenticationFilter(authenticationManager) {

    companion object {
        private val secretKey = System.getenv("MY_SECRET_KEY")
        private val key by lazy { Keys.hmacShaKeyFor(secretKey.toByteArray()) }
    }
    override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, chain: FilterChain) {

        if (request.requestURI == "/auth/signup") {
            chain.doFilter(request, response)
            return
        }

        val token =  request.cookies.find { it.name == "token" }?.value
        if (token == null) {
            chain.doFilter(request, response)
            return
        }

        val authentication: UsernamePasswordAuthenticationToken? = getAuthentication(request)
        SecurityContextHolder.getContext().authentication = authentication

        chain.doFilter(request, response)
    }

    private fun getAuthentication(request: HttpServletRequest): UsernamePasswordAuthenticationToken? {
        val token = request.cookies.find { it.name == "token" }?.value
        if (token != null) {
            val user: String = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .body
                .subject

            if (user != null) {
                return UsernamePasswordAuthenticationToken(user, null, ArrayList())
            }
            return null
        }
        return null
    }
}