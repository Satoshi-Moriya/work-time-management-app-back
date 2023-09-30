package com.example.worktimemanagement.security

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpHeaders
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource


@Configuration
@EnableWebSecurity
@EnableMethodSecurity
class WebSecurityConfig(private val myUserDetailsService: MyUserDetailsService) {

    @Autowired
    private lateinit var myLogoutSuccessHandler: MyLogoutSuccessHandler

    @Value("\${cors.allowedOrigins}")
    private lateinit var allowedOrigins: String

    @Throws(Exception::class)
    @Bean
    fun filterChain(http: HttpSecurity, authenticationManager: AuthenticationManager): SecurityFilterChain {

        http
            .cors {  }
            .authorizeHttpRequests {
                authorize -> authorize
                    .requestMatchers("/login", "/auth/signup", "/refresh-token", "/csrf").permitAll()
                    .anyRequest().authenticated()
            }
            .csrf {
                csrf -> csrf
                    .ignoringRequestMatchers("/csrf")
            }
            .addFilter(MyAuthenticationFilter(authenticationManager, bCryptPasswordEncoder(), myUserDetailsService))
            .addFilter(MyAuthorizationFilter(authenticationManager))
            .sessionManagement{session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            }
            .logout {
                    logout -> logout
                        .deleteCookies("accessToken", "refreshToken")
                        .logoutSuccessHandler(myLogoutSuccessHandler)
            }

        return http.build()
    }

    @Bean
    @Throws(java.lang.Exception::class)
    fun authenticationManager(authenticationConfiguration: AuthenticationConfiguration): AuthenticationManager? {
        return authenticationConfiguration.authenticationManager
    }

    @Bean
    fun bCryptPasswordEncoder() = BCryptPasswordEncoder()

    @Bean
    fun corsConfigurationSource(): CorsConfigurationSource? {
        val corsConfiguration = CorsConfiguration()
        corsConfiguration.addAllowedMethod(CorsConfiguration.ALL)
        corsConfiguration.addAllowedHeader(CorsConfiguration.ALL)
        corsConfiguration.addExposedHeader(HttpHeaders.AUTHORIZATION)
        corsConfiguration.addAllowedOrigin(allowedOrigins)
        corsConfiguration.allowCredentials = true
        val corsSource = UrlBasedCorsConfigurationSource()
        corsSource.registerCorsConfiguration("/**", corsConfiguration)
        return corsSource
    }
}
