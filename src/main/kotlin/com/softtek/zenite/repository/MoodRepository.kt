package com.softtek.zenite.repository

import com.softtek.zenite.entity.MoodDocument
import org.springframework.data.mongodb.repository.MongoRepository
import java.time.LocalDate

interface MoodRepository : MongoRepository<MoodDocument, String> {
    fun findByCodeOrderByCreatedAtDesc(code: String): List<MoodDocument>
    fun findByCodeAndDateBetweenOrderByCreatedAtDesc(
        code: String,
        start: LocalDate,
        end: LocalDate
    ): List<MoodDocument>
}