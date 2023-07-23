package com.example.worktimemanagement.security

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.web.SecurityFilterChain

@Configuration
@EnableWebSecurity
class WebSecurityConfig(val userDetailsService: UserDetailsService) {

    @Throws(Exception::class)
    @Bean
    fun filterChain(http: HttpSecurity, authenticationManager: AuthenticationManager): SecurityFilterChain {

        http
            .cors {  }
            .authorizeHttpRequests {
                authorize -> authorize
                    .requestMatchers("/login", "/auth/signup").permitAll()
                    .anyRequest().authenticated()
            }
            .logout{}
            .csrf {
                csrf -> csrf.disable()
            }
            .addFilter(MyAuthenticationFilter(authenticationManager, bCryptPasswordEncoder()))
            .addFilter(MyAuthorizationFilter(authenticationManager))
            .sessionManagement{session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            };

        return http.build()
    }

    @Bean
    @Throws(java.lang.Exception::class)
    fun authenticationManager(authenticationConfiguration: AuthenticationConfiguration): AuthenticationManager? {
        return authenticationConfiguration.authenticationManager
    }

    @Bean
    fun bCryptPasswordEncoder() = BCryptPasswordEncoder()
}
