package com.softtek.zenite.entity

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document
import java.time.OffsetDateTime

@Document("questionnaire_submissions")
data class SubmissionDocument(
    @Id var id: String? = null,
    @Indexed var code: String,
    @Indexed var questionnaireId: String,
    var answers: List<AnswerEmb> = emptyList(),
    var submittedAt: OffsetDateTime = OffsetDateTime.now()
)

data class AnswerEmb(
    var questionId: String,
    var answer: String
)