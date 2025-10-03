package com.softtek.zenite.entity

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDate
import java.time.OffsetDateTime

@Document("moods")
data class MoodDocument(
    @Id var id: String? = null,
    var code: String,
    var mood: String,
    var description: String? = null,
    var date: LocalDate? = null,
    var createdAt: OffsetDateTime = OffsetDateTime.now()
)