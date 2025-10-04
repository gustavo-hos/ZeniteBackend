package com.softtek.zenite.api.dto

import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotEmpty

data class QuestionnairesResponse(
    val questionnaires: List<QuestionnaireData>
)

data class QuestionnaireResponse(
    val questionnaire: QuestionnaireData
)

data class QuestionnaireData(
    @JsonProperty("_id") val id: String,
    val title: String,
    val description: String? = null,
    val questions: List<QuestionData>
)

data class QuestionData(
    @JsonProperty("_id") val id: String,
    val text: String,
    val type: String? = null,
    val options: List<String>? = null
)

data class AnswerRequest(
    @field:NotBlank val code: String,
    @field:NotEmpty(message = "answers must not be empty")
    val answers: List<AnswerData>
)

data class AnswerData(
    val questionId: String,
    val answer: String
)

data class AnswerSubmissionResponse(
    val success: Boolean,
    val submission: SubmissionData
)

data class UserAnswersResponse(
    val answers: List<SubmissionData>
)

data class SubmissionData(
    @JsonProperty("_id") val id: String,
    val code: String,
    val questionnaireId: String,
    val answers: List<AnswerWithQuestionData>,
    val submittedAt: String
)

data class AnswerWithQuestionData(
    val questionId: String,
    val question: String,
    val answer: String
)
