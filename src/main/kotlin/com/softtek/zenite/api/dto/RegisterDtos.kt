package com.softtek.zenite.api.dto

import com.nimbusds.jose.shaded.gson.annotations.SerializedName

data class RegisterRequest(
    val password: String,
    val words: Int? = null
)

data class RegisterResponse(
    val code: String,
    @SerializedName("master_passphrase") val masterPassphrase: String
)