//package com.example.worktimemanagement.security
//
//import org.springframework.http.HttpHeaders
//import org.springframework.security.authentication.AuthenticationManager
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
//import org.springframework.security.core.context.SecurityContextHolder
//import org.springframework.security.web.authentication.www.BasicAuthenticationFilter
//import jakarta.servlet.FilterChain
//import jakarta.servlet.http.HttpServletRequest
//import jakarta.servlet.http.HttpServletResponse
//
//class AuthorizationFilter(authenticationManager: AuthenticationManager):
//    BasicAuthenticationFilter(authenticationManager) {
//    override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, chain: FilterChain) {
//        val token = request.getHeader(HttpHeaders.AUTHORIZATION).let {
//            if (it == null || !it.startsWith("Bearer ")) {
//                chain.doFilter(request, response)
//                return
//            }
//            it.substring(7)
//        }
//
//        if(!JWTUtils.validateToken(token)) {
//            chain.doFilter(request, response)
//            return
//        }
//
//        val username = JWTUtils.getUsernameFromToken(token)
//        val authentication = UsernamePasswordAuthenticationToken(username,null, emptyList())
//        SecurityContextHolder.getContext().authentication = authentication
//
//        chain.doFilter(request, response)
//    }
//}