package com.softtek.zenite.api.controller

import LoginRequest
import LoginResponse
import UserSummary
import com.softtek.zenite.api.dto.RegisterRequest
import com.softtek.zenite.api.dto.RegisterResponse
import com.softtek.zenite.security.JwtProperties
import com.softtek.zenite.security.JwtService
import com.softtek.zenite.service.RegistrationService
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.oauth2.jwt.JwtDecoder
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.time.Instant

@RestController
@RequestMapping("/api/v1/auth")
class AuthController(
    private val authenticationManager: AuthenticationManager,
    private val userDetailsService: UserDetailsService,
    private val registrationService: RegistrationService,
    private val jwtService: JwtService,
    private val jwtDecoder: JwtDecoder,
    private val props: JwtProperties
) {

    @PostMapping("/login")
    fun login(@RequestBody req: LoginRequest): ResponseEntity<LoginResponse> {
        val auth = UsernamePasswordAuthenticationToken(req.code, req.password)
        val result = authenticationManager.authenticate(auth)
        val principal = result.principal as UserDetails
        val roles = principal.authorities.map { it.authority.removePrefix("ROLE_") }

        val access = jwtService.issueAccessToken(principal.username, roles)
        val refresh = jwtService.issueRefreshToken(principal.username)

        val body = LoginResponse(
            accessToken = access,
            expiresIn = props.accessTokenTtlSeconds,
            refreshToken = refresh,
            user = UserSummary(code = principal.username, roles = roles)
        )
        return ResponseEntity.ok(body)
    }

    @PostMapping("/refresh")
    fun refresh(@RequestBody payload: Map<String, String>): ResponseEntity<Map<String, Any>> {
        val token = payload["refresh_token"] ?: return ResponseEntity.badRequest()
            .body(mapOf("error" to "missing refresh_token"))

        val jwt = jwtDecoder.decode(token)
        if (jwt.expiresAt?.isBefore(Instant.now()) == true) {
            return ResponseEntity.status(401).body(mapOf("error" to "refresh_token expired"))
        }
        if (jwt.claims["type"] != "refresh") {
            return ResponseEntity.status(400).body(mapOf("error" to "invalid token type"))
        }
        val username = jwt.subject
        val user = userDetailsService.loadUserByUsername(username)
        val roles = user.authorities.map { it.authority.removePrefix("ROLE_") }

        val newAccess = jwtService.issueAccessToken(username, roles)
        return ResponseEntity.ok(
            mapOf(
                "access_token" to newAccess,
                "token_type" to "Bearer",
                "expires_in" to props.accessTokenTtlSeconds
            )
        )
    }

    @PostMapping("/register")
    fun register(@RequestBody req: RegisterRequest): ResponseEntity<RegisterResponse> {
        val result = registrationService.register(req.password, req.words)
        return ResponseEntity.ok(RegisterResponse(result.code, result.masterPassphrase))
    }
}