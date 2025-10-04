package com.softtek.zenite.api.controller

import com.softtek.zenite.api.QuestionnaireApi
import com.softtek.zenite.api.dto.*
import com.softtek.zenite.service.QuestionnaireService
import org.springframework.web.bind.annotation.RestController

@RestController
class QuestionnaireController(
    private val questionnaireService: QuestionnaireService
) : QuestionnaireApi {

    override fun listAll(): QuestionnairesResponse = questionnaireService.listAll()

    override fun getOne(id: String): QuestionnaireResponse = questionnaireService.getOne(id)

    override fun submitAnswers(id: String, rq: AnswerRequest): AnswerSubmissionResponse =
        questionnaireService.submitAnswers(id, rq)

    override fun getUserAnswers(code: String): UserAnswersResponse =
        questionnaireService.getUserAnswers(code)
}