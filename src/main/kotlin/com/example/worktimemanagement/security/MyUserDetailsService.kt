package com.example.worktimemanagement.security

import com.example.worktimemanagement.entity.User
import com.example.worktimemanagement.repository.UserRepository
import org.slf4j.LoggerFactory
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class MyUserDetailsService(private val userRepository: UserRepository): UserDetailsService {

    private val logger = LoggerFactory.getLogger(MyUserDetailsService::class.java)

    @Throws(UsernameNotFoundException::class)
    override fun loadUserByUsername(email: String): UserDetails {
        if (email == null) {
            throw UsernameNotFoundException("ユーザーが見つかりませんでした: $email")
        }

        return MyUserDetails(userRepository.findByUserEmail(email))
    }
}