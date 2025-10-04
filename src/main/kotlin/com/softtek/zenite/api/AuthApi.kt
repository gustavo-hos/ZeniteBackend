package com.softtek.zenite.api

import LoginRequest
import LoginResponse
import com.softtek.zenite.api.dto.RegisterRequest
import com.softtek.zenite.api.dto.RegisterResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.parameters.RequestBody
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@Tag(name = "Autenticação", description = "Fluxos de login, refresh e registro")
@RequestMapping("/api/v1/auth")
interface AuthApi {

    @Operation(
        summary = "Login",
        description = "Autentica por código e senha e retorna tokens de acesso e refresh",
        responses = [
            ApiResponse(responseCode = "200", description = "OK"),
            ApiResponse(responseCode = "401", description = "Credenciais inválidas", content = [Content()])
        ]
    )
    @PostMapping("/login")
    fun login(
        @RequestBody(
            description = "Credenciais de acesso",
            required = true,
            content = [Content(schema = Schema(implementation = LoginRequest::class))]
        )
        @org.springframework.web.bind.annotation.RequestBody
        req: LoginRequest
    ): ResponseEntity<LoginResponse>

    @Operation(
        summary = "Refresh token",
        description = "Gera novo access token a partir de um refresh token válido",
        responses = [
            ApiResponse(responseCode = "200", description = "OK"),
            ApiResponse(responseCode = "400", description = "Token inválido", content = [Content()]),
            ApiResponse(responseCode = "401", description = "Refresh expirado", content = [Content()])
        ]
    )
    @PostMapping("/refresh")
    fun refresh(
        @RequestBody(
            description = """Payload: {"refresh_token": "<token>"}""",
            required = true
        )
        @org.springframework.web.bind.annotation.RequestBody
        payload: Map<String, String>
    ): ResponseEntity<Map<String, Any>>

    @Operation(
        summary = "Registrar usuário",
        description = "Cria um novo usuário e retorna código e passphrase mestre",
        responses = [ApiResponse(responseCode = "200", description = "OK")]
    )
    @PostMapping("/register")
    fun register(
        @RequestBody(
            description = "Dados de registro",
            required = true,
            content = [Content(schema = Schema(implementation = RegisterRequest::class))]
        )
        @org.springframework.web.bind.annotation.RequestBody
        req: RegisterRequest
    ): ResponseEntity<RegisterResponse>
}
