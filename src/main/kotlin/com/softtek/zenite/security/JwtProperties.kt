package com.softtek.zenite.security

import com.nimbusds.jose.JWSAlgorithm
import com.nimbusds.jose.jwk.JWKSet
import com.nimbusds.jose.jwk.KeyUse
import com.nimbusds.jose.jwk.OctetSequenceKey
import com.nimbusds.jose.jwk.source.ImmutableJWKSet
import com.nimbusds.jose.proc.SecurityContext
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.oauth2.jose.jws.MacAlgorithm
import org.springframework.security.oauth2.jwt.JwtDecoder
import org.springframework.security.oauth2.jwt.JwtEncoder
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder
import java.util.*
import javax.crypto.spec.SecretKeySpec

@ConfigurationProperties(prefix = "jwt")
data class JwtProperties(
    var secret: String = "",
    var accessTokenTtlSeconds: Long = 900,
    var refreshTokenTtlSeconds: Long = 2_592_000
)

@Configuration
@EnableConfigurationProperties(JwtProperties::class)
class JwtConfig(private val props: JwtProperties) {

    @Bean
    fun jwtEncoder(): JwtEncoder {
        val keyBytes = Base64.getDecoder().decode(props.secret)
        require(keyBytes.size >= 32) { "JWT secret deve ser >= 32 bytes depois do decode." }

        val jwk = OctetSequenceKey.Builder(keyBytes)
            .keyUse(KeyUse.SIGNATURE)
            .algorithm(JWSAlgorithm.HS256)
            .keyID("hs256")
            .build()

        val jwkSource = ImmutableJWKSet<SecurityContext>(JWKSet(jwk))
        return NimbusJwtEncoder(jwkSource)
    }

    @Bean
    fun jwtDecoder(): JwtDecoder {
        val keyBytes = Base64.getDecoder().decode(props.secret)
        require(keyBytes.size >= 32) { "JWT secret deve ser >= 32 bytes depois do decode." }
        val secretKey = SecretKeySpec(keyBytes, "HmacSHA256")
        return NimbusJwtDecoder
            .withSecretKey(secretKey)
            .macAlgorithm(MacAlgorithm.HS256)
            .build()
    }
}