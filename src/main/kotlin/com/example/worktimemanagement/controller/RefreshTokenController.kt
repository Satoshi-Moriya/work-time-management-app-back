package com.example.worktimemanagement.controller

import com.example.worktimemanagement.dto.UserIssueToken
import com.example.worktimemanagement.error.ExpiredRefreshTokenException
import com.example.worktimemanagement.repository.RefreshTokenRepository
import com.example.worktimemanagement.security.MyUserDetailsService
import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import jakarta.servlet.http.Cookie
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.web.csrf.CsrfToken
import org.springframework.web.bind.annotation.*


@RestController
@CrossOrigin
class RefreshTokenController(
    @Value("\${jwt.accesstoken.secretkey}")
    private val accessTokenSecret: String,
    private val myUserDetailsService: MyUserDetailsService,
    private val refreshTokenRepository: RefreshTokenRepository
) {

    @PostMapping("/refresh-token")
    fun refreshToken(request: HttpServletRequest, response: HttpServletResponse): UserIssueToken {
        val accessToken =  request.cookies.find { it.name == "accessToken" }?.value
        val refreshToken =  request.cookies.find { it.name == "refreshToken" }?.value
        val key = Keys.hmacShaKeyFor(accessTokenSecret.toByteArray())

        // accessTokenが有効かどうかのチェック（有効期限切れはOK）
        val jwtClaims = try {
            Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(accessToken)
                .body
        } catch (e: ExpiredJwtException) {
            e.claims
        } catch (e: Exception) {
            throw e
        }

        // accessTokenがあるからrefreshTokenはnullではない
        if (myUserDetailsService.verifyRefreshToken(jwtClaims.subject, refreshToken!!)) {
            val newToken = myUserDetailsService.issueToken(jwtClaims.subject)
            val accessTokenCookie = createCookie("accessToken", newToken.accessToken)
            // ToDo refreshTokenをここで更新するべきなのか？（もっと長い期限がを与えるはずだからaccessTokenだけの更新でよくない？）
            val refreshTokenCookie = createCookie("refreshToken", newToken.refreshToken)
            response.addCookie(accessTokenCookie)
            response.addCookie(refreshTokenCookie)
            return newToken
        } else {
            // refreshTokenの期限切れだから、refreshTokenがDBにないことはない!!
            val deleteRefreshToken = refreshTokenRepository.findByUserEmail(jwtClaims.subject)
            refreshTokenRepository.delete(deleteRefreshToken!!)
            throw ExpiredRefreshTokenException("トークンの有効期限がきれました。ログインし直してください。")
        }
    }

    private fun createCookie(name: String, value: String): Cookie {
        return Cookie(name, value).apply {
            path = "/"
            isHttpOnly = true
        }
    }

    @PostMapping("/csrf")
    fun csrf(token: CsrfToken?): CsrfToken? {
        return token
    }
}