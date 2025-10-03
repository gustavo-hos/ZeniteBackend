package com.softtek.zenite.controller

import com.nimbusds.jose.shaded.gson.annotations.SerializedName
import com.softtek.zenite.service.RegistrationService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/auth")
class RegistrationController(
    private val registrationService: RegistrationService
) {
    data class RegisterRequest(
        val password: String,
        val words: Int? = null
    )

    data class RegisterResponse(
        val code: String,
        @SerializedName("master_passphrase") val masterPassphrase: String
    )

    @PostMapping("/register")
    fun register(@RequestBody req: RegisterRequest): ResponseEntity<RegisterResponse> {
        val result = registrationService.register(req.password, req.words)
        return ResponseEntity.ok(RegisterResponse(result.code, result.masterPassphrase))
    }
}