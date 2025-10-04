package com.softtek.zenite.api.dto.controller

import com.nimbusds.jose.shaded.gson.annotations.SerializedName
import com.softtek.zenite.service.RegistrationService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/auth")
class RegistrationController(
    private val registrationService: RegistrationService
) {

}