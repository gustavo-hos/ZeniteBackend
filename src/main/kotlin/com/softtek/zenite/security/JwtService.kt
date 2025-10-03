package com.softtek.zenite.security

import org.springframework.security.oauth2.jose.jws.MacAlgorithm
import org.springframework.security.oauth2.jwt.JwtClaimsSet
import org.springframework.security.oauth2.jwt.JwtEncoder
import org.springframework.security.oauth2.jwt.JwtEncoderParameters
import org.springframework.security.oauth2.jwt.JwsHeader
import org.springframework.stereotype.Service
import java.time.Instant

@Service
class JwtService(
    private val jwtEncoder: JwtEncoder,
    private val props: JwtProperties
) {
    fun issueAccessToken(subject: String, roles: Collection<String>): String {
        val now = Instant.now()
        val claims = JwtClaimsSet.builder()
            .subject(subject)
            .issuedAt(now)
            .expiresAt(now.plusSeconds(props.accessTokenTtlSeconds))
            .claim("roles", roles)
            .claim("type", "access")
            .build()

        val header = JwsHeader.with(MacAlgorithm.HS256).build()  // <---
        return jwtEncoder.encode(JwtEncoderParameters.from(header, claims)).tokenValue
    }

    fun issueRefreshToken(subject: String): String {
        val now = Instant.now()
        val claims = JwtClaimsSet.builder()
            .subject(subject)
            .issuedAt(now)
            .expiresAt(now.plusSeconds(props.refreshTokenTtlSeconds))
            .claim("type", "refresh")
            .build()

        val header = JwsHeader.with(MacAlgorithm.HS256).build()  // <---
        return jwtEncoder.encode(JwtEncoderParameters.from(header, claims)).tokenValue
    }
}