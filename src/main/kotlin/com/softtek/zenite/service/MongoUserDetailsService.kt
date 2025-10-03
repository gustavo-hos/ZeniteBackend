package com.softtek.zenite.service

import com.softtek.zenite.repository.AuthUserRepository
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class MongoUserDetailsService(
    private val repo: AuthUserRepository
) : UserDetailsService {

    override fun loadUserByUsername(username: String): UserDetails {
        val u = repo.findByCode(username)
            ?: throw UsernameNotFoundException("User not found: $username")
        return User.withUsername(u.code)
            .password(u.passwordHash)
            .roles(*u.roles.ifEmpty { setOf("USER") }.toTypedArray())
            .accountLocked(false)
            .disabled(false)
            .build()
    }
}