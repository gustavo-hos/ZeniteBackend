package com.softtek.zenite.service

import com.softtek.zenite.api.BadRequestException
import com.softtek.zenite.api.NotFoundException
import com.softtek.zenite.api.dto.*
import com.softtek.zenite.api.mapper.QuestionnaireMapper
import com.softtek.zenite.entity.AnswerEmb
import com.softtek.zenite.entity.QuestionnaireDocument
import com.softtek.zenite.entity.SubmissionDocument
import com.softtek.zenite.repository.QuestionnaireRepository
import com.softtek.zenite.repository.SubmissionRepository
import org.springframework.stereotype.Service
import java.time.format.DateTimeFormatter

@Service
class QuestionnaireService(
    private val questionnaires: QuestionnaireRepository,
    private val submissions: SubmissionRepository,
    private val mapper: QuestionnaireMapper
) {

    fun listAll(): QuestionnairesResponse =
        QuestionnairesResponse(mapper.toDtoList(questionnaires.findAll()))

    fun getOne(id: String): QuestionnaireResponse {
        val doc =
            questionnaires.findById(id).orElseThrow { NotFoundException("questionnaire not found") }
        return QuestionnaireResponse(mapper.toDto(doc))
    }

    fun submitAnswers(questionnaireId: String, rq: AnswerRequest): AnswerSubmissionResponse {
        val q = questionnaires.findById(questionnaireId)
            .orElseThrow { NotFoundException("questionnaire not found") }

        val validIds = q.questions.mapNotNull { it.id }.toSet()
        if (rq.answers.isEmpty()) throw BadRequestException("answers cannot be empty")
        rq.answers.forEach {
            if (!validIds.contains(it.questionId)) {
                throw BadRequestException("invalid questionId: ${it.questionId}")
            }
        }

        val toSave = SubmissionDocument(
            code = rq.code,
            questionnaireId = questionnaireId,
            answers = rq.answers.map { AnswerEmb(it.questionId, it.answer) }
        )
        val saved = submissions.save(toSave)

        val dto = toSubmissionData(saved, q)
        return AnswerSubmissionResponse(success = true, submission = dto)
    }

    fun getUserAnswers(code: String): UserAnswersResponse {
        val list = submissions.findByCodeOrderBySubmittedAtDesc(code)
        val byQuestionnaire = questionnaires.findAll().associateBy { it.id }
        val dtos = list.map { sub ->
            val q = byQuestionnaire[sub.questionnaireId]
                ?: throw NotFoundException("questionnaire ${sub.questionnaireId} not found")
            toSubmissionData(sub, q)
        }
        return UserAnswersResponse(dtos)
    }

    private fun toSubmissionData(
        sub: SubmissionDocument,
        q: QuestionnaireDocument
    ): SubmissionData {
        val questionTextById = q.questions.associate { (it.id ?: "") to it.text }
        val enriched = sub.answers.map { a ->
            AnswerWithQuestionData(
                questionId = a.questionId,
                question = questionTextById[a.questionId] ?: "",
                answer = a.answer
            )
        }
        val iso = sub.submittedAt.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME)
        return SubmissionData(
            id = requireNotNull(sub.id),
            code = sub.code,
            questionnaireId = sub.questionnaireId,
            answers = enriched,
            submittedAt = iso
        )
    }
}