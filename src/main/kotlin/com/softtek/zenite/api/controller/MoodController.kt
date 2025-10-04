package com.softtek.zenite.api.controller

import com.softtek.zenite.api.ForbiddenException
import com.softtek.zenite.api.MoodApi
import com.softtek.zenite.api.dto.MoodRequest
import com.softtek.zenite.api.dto.MoodResponse
import com.softtek.zenite.api.dto.MoodsResponse
import com.softtek.zenite.service.MoodService
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDate

@RestController
class MoodController(private val svc: MoodService) : MoodApi {

    override fun create(rq: MoodRequest, auth: Authentication?): MoodResponse {
        mustBeOwner(auth, rq.code)
        return MoodResponse(svc.create(rq))
    }

    override fun list(
        code: String,
        startDate: LocalDate?,
        endDate: LocalDate?,
        auth: Authentication?
    ): MoodsResponse {
        mustBeOwner(auth, code)
        return MoodsResponse(svc.list(code, startDate, endDate))
    }

    override fun get(code: String, id: String, auth: Authentication?): MoodResponse {
        mustBeOwner(auth, code)
        return MoodResponse(svc.get(code, id))
    }

    override fun update(
        code: String,
        id: String,
        rq: MoodRequest,
        auth: Authentication?
    ): MoodResponse {
        mustBeOwner(auth, code)
        return MoodResponse(svc.update(code, id, rq))
    }

    override fun delete(code: String, id: String, auth: Authentication?) {
        mustBeOwner(auth, code)
        svc.delete(code, id)
    }

    private fun mustBeOwner(auth: Authentication?, code: String) {
        if (auth?.name != code) throw ForbiddenException()
    }
}