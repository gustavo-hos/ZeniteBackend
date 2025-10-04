package com.softtek.zenite.api

import com.softtek.zenite.api.dto.MoodRequest
import com.softtek.zenite.api.dto.MoodResponse
import com.softtek.zenite.api.dto.MoodsResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.parameters.RequestBody
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.security.core.Authentication
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import java.time.LocalDate

@Tag(name = "Humor", description = "Registro e consulta de humores")
@RequestMapping("/api/v1/moods")
@Validated
interface MoodApi {

    @Operation(
        summary = "Criar humor",
        description = "Cria um registro de humor para o usuário",
        responses = [ApiResponse(responseCode = "200", description = "OK")]
    )
    @PostMapping(consumes = [MediaType.APPLICATION_JSON_VALUE], produces = [MediaType.APPLICATION_JSON_VALUE])
    fun create(
        @RequestBody(
            required = true,
            description = "Dados do humor",
            content = [Content(schema = Schema(implementation = MoodRequest::class))]
        )
        @org.springframework.web.bind.annotation.RequestBody
        rq: MoodRequest,
        @Parameter(hidden = true) auth: Authentication?
    ): MoodResponse

    @Operation(
        summary = "Listar humores do usuário",
        description = "Lista humores por código do usuário e intervalo de datas",
        responses = [ApiResponse(responseCode = "200", description = "OK")]
    )
    @GetMapping("/{code}", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun list(
        @PathVariable code: String,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) startDate: LocalDate?,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) endDate: LocalDate?,
        @Parameter(hidden = true) auth: Authentication?
    ): MoodsResponse

    @Operation(
        summary = "Obter humor",
        description = "Retorna um registro de humor por ID",
        responses = [ApiResponse(responseCode = "200", description = "OK")]
    )
    @GetMapping("/{code}/{id}", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun get(
        @PathVariable code: String,
        @PathVariable id: String,
        @Parameter(hidden = true) auth: Authentication?
    ): MoodResponse

    @Operation(
        summary = "Atualizar humor",
        description = "Atualiza um registro de humor por ID",
        responses = [ApiResponse(responseCode = "200", description = "OK")]
    )
    @PutMapping("/{code}/{id}", consumes = [MediaType.APPLICATION_JSON_VALUE], produces = [MediaType.APPLICATION_JSON_VALUE])
    fun update(
        @PathVariable code: String,
        @PathVariable id: String,
        @RequestBody(
            required = true,
            description = "Dados do humor",
            content = [Content(schema = Schema(implementation = MoodRequest::class))]
        )
        @org.springframework.web.bind.annotation.RequestBody
        rq: MoodRequest,
        @Parameter(hidden = true) auth: Authentication?
    ): MoodResponse

    @Operation(
        summary = "Excluir humor",
        description = "Exclui um registro de humor por ID",
        responses = [ApiResponse(responseCode = "204", description = "Sem conteúdo")]
    )
    @DeleteMapping("/{code}/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun delete(
        @PathVariable code: String,
        @PathVariable id: String,
        @Parameter(hidden = true) auth: Authentication?
    )
}
