package com.softtek.zenite.repository

import com.softtek.zenite.entity.SubmissionDocument
import org.springframework.data.mongodb.repository.MongoRepository

interface SubmissionRepository : MongoRepository<SubmissionDocument, String> {
    fun findByCodeOrderBySubmittedAtDesc(code: String): List<SubmissionDocument>
    fun findByQuestionnaireId(questionnaireId: String): List<SubmissionDocument>
}