package com.softtek.zenite.api

import com.softtek.zenite.api.dto.*
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.parameters.RequestBody
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping

@Tag(name = "Questionários", description = "Consulta e submissão de respostas")
@RequestMapping("/api/v1/questionnaires")
interface QuestionnaireApi {

    @Operation(
        summary = "Listar questionários",
        responses = [ApiResponse(responseCode = "200", description = "OK")]
    )
    @GetMapping(produces = [MediaType.APPLICATION_JSON_VALUE])
    fun listAll(): QuestionnairesResponse

    @Operation(
        summary = "Obter questionário por ID",
        responses = [ApiResponse(responseCode = "200", description = "OK")]
    )
    @GetMapping("/{id}", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun getOne(@PathVariable id: String): QuestionnaireResponse

    @Operation(
        summary = "Enviar respostas",
        description = "Submete respostas de um usuário para um questionário",
        responses = [ApiResponse(responseCode = "200", description = "OK")]
    )
    @PostMapping(
        "/{id}/answers",
        consumes = [MediaType.APPLICATION_JSON_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun submitAnswers(
        @PathVariable id: String,
        @RequestBody(
            required = true,
            description = "Respostas do usuário",
            content = [Content(schema = Schema(implementation = AnswerRequest::class))]
        )
        @org.springframework.web.bind.annotation.RequestBody
        rq: AnswerRequest
    ): AnswerSubmissionResponse

    @Operation(
        summary = "Respostas do usuário",
        description = "Lista respostas por código do usuário"
    )
    @GetMapping("/answers/{code}", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun getUserAnswers(@PathVariable code: String): UserAnswersResponse
}
