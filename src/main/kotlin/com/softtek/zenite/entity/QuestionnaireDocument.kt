package com.softtek.zenite.entity

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document("questionnaires")
data class QuestionnaireDocument(
    @Id var id: String? = null,
    var title: String,
    var description: String? = null,
    var questions: List<QuestionEmb> = emptyList()
)

data class QuestionEmb(
    var id: String? = null,
    var text: String,
    var type: String? = null,
    var options: List<String>? = null
)