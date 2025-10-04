package com.softtek.zenite.api.controller

import com.softtek.zenite.api.dto.*
import com.softtek.zenite.service.QuestionnaireService
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/questionnaires")
class QuestionnaireController(
    private val questionnaireService: QuestionnaireService
) {
    @GetMapping
    fun listAll(): QuestionnairesResponse = questionnaireService.listAll()

    @GetMapping("/{id}")
    fun getOne(@PathVariable id: String): QuestionnaireResponse = questionnaireService.getOne(id)

    @PostMapping("/{id}/answers")
    fun submitAnswers(
        @PathVariable id: String,
        @Valid @RequestBody rq: AnswerRequest
    ): AnswerSubmissionResponse =
        questionnaireService.submitAnswers(id, rq)

    @GetMapping("/answers/{code}")
    fun getUserAnswers(@PathVariable code: String): UserAnswersResponse = questionnaireService.getUserAnswers(code)
}