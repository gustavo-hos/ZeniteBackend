package com.softtek.zenite.api.controller

import com.softtek.zenite.api.ForbiddenException
import com.softtek.zenite.api.dto.MoodRequest
import com.softtek.zenite.api.dto.MoodResponse
import com.softtek.zenite.api.dto.MoodsResponse
import com.softtek.zenite.service.MoodService
import jakarta.validation.Valid
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.http.HttpStatus
import org.springframework.security.core.Authentication
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import java.time.LocalDate

@RestController
@RequestMapping("/api/v1/moods")
@Validated
class MoodController(
    private val svc: MoodService
) {
    @PostMapping
    fun create(@Valid @RequestBody rq: MoodRequest, auth: Authentication?): MoodResponse {
        mustBeOwner(auth, rq.code)
        return MoodResponse(svc.create(rq))
    }

    @GetMapping("/{code}")
    fun list(
        @PathVariable code: String,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) startDate: LocalDate?,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) endDate: LocalDate?,
        auth: Authentication?
    ): MoodsResponse {
        mustBeOwner(auth, code)
        return MoodsResponse(svc.list(code, startDate, endDate))
    }

    @GetMapping("/{code}/{id}")
    fun get(
        @PathVariable code: String,
        @PathVariable id: String,
        auth: Authentication?
    ): MoodResponse {
        mustBeOwner(auth, code)
        return MoodResponse(svc.get(code, id))
    }

    @PutMapping("/{code}/{id}")
    fun update(
        @PathVariable code: String,
        @PathVariable id: String,
        @Valid @RequestBody rq: MoodRequest,
        auth: Authentication?
    ): MoodResponse {
        mustBeOwner(auth, code)
        return MoodResponse(svc.update(code, id, rq))
    }

    @DeleteMapping("/{code}/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun delete(@PathVariable code: String, @PathVariable id: String, auth: Authentication?) {
        mustBeOwner(auth, code)
        svc.delete(code, id)
    }

    private fun mustBeOwner(auth: Authentication?, code: String) {
        if (auth?.name != code) throw ForbiddenException()
    }
}
