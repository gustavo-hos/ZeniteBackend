package com.softtek.zenite.service

import com.softtek.zenite.api.ForbiddenException
import com.softtek.zenite.api.NotFoundException
import com.softtek.zenite.api.dto.MoodData
import com.softtek.zenite.api.dto.MoodRequest
import com.softtek.zenite.api.mapper.MoodMapper
import com.softtek.zenite.repository.MoodRepository
import org.springframework.stereotype.Service
import java.time.LocalDate

@Service
class MoodService(
    private val repo: MoodRepository,
    private val mapper: MoodMapper
) {
    fun create(rq: MoodRequest): MoodData {
        val doc = mapper.toDocument(rq)
        val saved = repo.save(doc)
        return mapper.toData(saved)
    }

    fun list(code: String, start: LocalDate?, end: LocalDate?): List<MoodData> {
        val docs = if (start != null && end != null) {
            repo.findByCodeAndDateBetweenOrderByCreatedAtDesc(code, start, end)
        } else {
            repo.findByCodeOrderByCreatedAtDesc(code)
        }
        return mapper.toDataList(docs)
    }

    fun get(code: String, id: String): MoodData {
        val doc = repo.findById(id).orElseThrow { NotFoundException() }
        if (doc.code != code) throw ForbiddenException()
        return mapper.toData(doc)
    }

    fun update(code: String, id: String, rq: MoodRequest): MoodData {
        val doc = repo.findById(id).orElseThrow { NotFoundException() }
        if (doc.code != code) throw ForbiddenException()
        mapper.updateDocument(doc, rq)
        val saved = repo.save(doc)
        return mapper.toData(saved)
    }

    fun delete(code: String, id: String) {
        val doc = repo.findById(id).orElseThrow { NotFoundException() }
        if (doc.code != code) throw ForbiddenException()
        repo.deleteById(id)
    }
}
