package com.softtek.zenite.api.dto

import jakarta.validation.constraints.NotBlank
import java.time.LocalDate
import java.time.OffsetDateTime

data class MoodRequest(
    @field:NotBlank val code: String,
    @field:NotBlank val mood: String,
    val description: String? = null,
    val date: LocalDate? = null
)

data class MoodData(
    val id: String,
    val code: String,
    val mood: String,
    val description: String?,
    val date: LocalDate?,
    val createdAt: OffsetDateTime
)

data class MoodResponse(val mood: MoodData)
data class MoodsResponse(val moods: List<MoodData>)