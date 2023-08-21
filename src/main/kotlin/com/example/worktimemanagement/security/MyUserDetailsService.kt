package com.example.worktimemanagement.security

import com.example.worktimemanagement.dto.UserIssueToken
import com.example.worktimemanagement.entity.RefreshToken
import com.example.worktimemanagement.entity.User
import com.example.worktimemanagement.repository.RefreshTokenRepository
import com.example.worktimemanagement.repository.UserRepository
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import io.micrometer.common.util.StringUtils
import org.apache.commons.lang3.RandomStringUtils
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.*


const val REFRESH_TOKEN_LENGTH = 24

@Service
class MyUserDetailsService(
    private val userRepository: UserRepository,
    private val refreshTokenRepository: RefreshTokenRepository,
    @Value("\${jwt.accesstoken.expirationtime}")
    private val accessTokenExpTime: Long,
    @Value("\${jwt.refreshtoken.expirationtime}")
    private val refreshTokenExpTime: Long,
    @Value("\${jwt.accesstoken.secretkey}")
    private val accessTokenSecret: String
): UserDetailsService {

    private val bCryptPasswordEncoder: BCryptPasswordEncoder = BCryptPasswordEncoder()

    private val logger = LoggerFactory.getLogger(MyUserDetailsService::class.java)

    @Throws(UsernameNotFoundException::class)
    override fun loadUserByUsername(email: String): UserDetails {
        if (email == null) {
            throw UsernameNotFoundException("ユーザーが見つかりませんでした: $email")
        }

        return MyUserDetails(userRepository.findByUserEmail(email).orElse(null))
    }

    // 新しいアクセストークンを発行する
    @Throws(UsernameNotFoundException::class)
    @Transactional
    fun issueToken(userEmail: String): UserIssueToken {
        val now: Instant = Instant.now()
        val key = Keys.hmacShaKeyFor(accessTokenSecret.toByteArray())

        val accessToken = Jwts.builder()
            .setSubject(userEmail)
            .setIssuedAt(Date.from(now))
            .setExpiration(Date.from(now.plusSeconds(accessTokenExpTime)))
            .signWith(key)
            .compact()

        return UserIssueToken(
            accessToken,
            generateRefreshToken(userEmail)
        )
    }

    @Throws(UsernameNotFoundException::class)
    @Transactional(readOnly = true)
    fun verifyRefreshToken(userEmail: String, refreshToken: String): Boolean {
        // ログインしてrefreshTokenを発行後にしかここを通らないからnon-nullでOK
        val refreshTokenEntity = refreshTokenRepository.findByUserEmail(userEmail)!!

        if(refreshTokenEntity.refreshTokenId != null &&
            refreshTokenEntity.refreshTokenIssuedAt.plus(refreshTokenExpTime, ChronoUnit.SECONDS).isBefore(Instant.now())) {
            logger.info("Refresh token of {} is already expired", userEmail);
            return false
        }

        println(refreshToken)
        println(refreshTokenEntity.refreshToken)
        println(StringUtils.isNotEmpty(refreshTokenEntity.refreshToken))
        return StringUtils.isNotEmpty(refreshTokenEntity.refreshToken) && bCryptPasswordEncoder.matches(refreshToken, refreshTokenEntity.refreshToken);
    }

    @Throws(UsernameNotFoundException::class)
    fun generateRefreshToken(userEmail: String): String {
        val refreshToken: String = RandomStringUtils.randomAlphanumeric(REFRESH_TOKEN_LENGTH)
        refreshTokenRepository.findByUserEmail(userEmail)
            ?.let { refreshTokenEntity ->
                refreshTokenEntity.refreshToken = bCryptPasswordEncoder.encode(refreshToken)
                refreshTokenEntity.refreshTokenIssuedAt = Instant.now()
                refreshTokenRepository.save(refreshTokenEntity)
            } ?: (
                refreshTokenRepository.save(
                    RefreshToken(
                        0,
                        userEmail,
                        bCryptPasswordEncoder.encode(refreshToken),
                        Instant.now()
                    )
                )
            )
        return refreshToken
    }

    @Throws(UsernameNotFoundException::class)
    fun findByUserEmail(userEmail: String): User {
        return userRepository.findByUserEmailNotOptional(userEmail)
            ?: throw UsernameNotFoundException("User not found:[$userEmail]")
    }
}