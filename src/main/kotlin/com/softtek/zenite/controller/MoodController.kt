package com.zenite.mood

import jakarta.validation.Valid
import jakarta.validation.constraints.NotBlank
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.http.HttpStatus
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Service
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import java.time.LocalDate
import java.time.OffsetDateTime

// =============== DTOs (Request/Response) ===============
data class MoodRequest(
    @field:NotBlank val code: String,
    @field:NotBlank val mood: String,
    val description: String? = null,
    val date: LocalDate? = null
)

data class MoodData(
    val id: String,
    val code: String,
    val mood: String,
    val description: String?,
    val date: LocalDate?,
    val createdAt: OffsetDateTime
)

data class MoodResponse(val mood: MoodData)
data class MoodsResponse(val moods: List<MoodData>)

// ================== Mongo Document =====================
@Document("moods")
data class MoodDocument(
    @Id var id: String? = null,
    var code: String,
    var mood: String,
    var description: String? = null,
    var date: LocalDate? = null,
    var createdAt: OffsetDateTime = OffsetDateTime.now()
)

// ==================== Repository =======================
interface MoodRepository : MongoRepository<MoodDocument, String> {
    fun findByCodeOrderByCreatedAtDesc(code: String): List<MoodDocument>
    fun findByCodeAndDateBetweenOrderByCreatedAtDesc(code: String, start: LocalDate, end: LocalDate): List<MoodDocument>
}

// ====================== Service ========================
@Service
class MoodService @Autowired constructor(
    private val repo: MoodRepository
) {
    fun create(rq: MoodRequest): MoodData {
        val saved = repo.save(
            MoodDocument(
                code = rq.code,
                mood = rq.mood,
                description = rq.description,
                date = rq.date
            )
        )
        return saved.toDto()
    }

    fun list(code: String, start: LocalDate?, end: LocalDate?): List<MoodData> {
        val docs = if (start != null && end != null) {
            repo.findByCodeAndDateBetweenOrderByCreatedAtDesc(code, start, end)
        } else {
            repo.findByCodeOrderByCreatedAtDesc(code)
        }
        return docs.map { it.toDto() }
    }

    fun get(code: String, id: String): MoodData {
        val doc = repo.findById(id).orElseThrow { NotFoundException() }
        if (doc.code != code) throw ForbiddenException()
        return doc.toDto()
    }

    fun update(code: String, id: String, rq: MoodRequest): MoodData {
        val doc = repo.findById(id).orElseThrow { NotFoundException() }
        if (doc.code != code) throw ForbiddenException()
        doc.mood = rq.mood
        doc.description = rq.description
        doc.date = rq.date
        return repo.save(doc).toDto()
    }

    fun delete(code: String, id: String) {
        val doc = repo.findById(id).orElseThrow { NotFoundException() }
        if (doc.code != code) throw ForbiddenException()
        repo.deleteById(id)
    }

    private fun MoodDocument.toDto() = MoodData(
        id = requireNotNull(this.id),
        code = this.code,
        mood = this.mood,
        description = this.description,
        date = this.date,
        createdAt = this.createdAt
    )
}

// ==================== Controller =======================
@RestController
@RequestMapping("/api/v1/moods")
@Validated
class MoodController @Autowired constructor(
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
    fun get(@PathVariable code: String, @PathVariable id: String, auth: Authentication?): MoodResponse {
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

// ================= Error Mapping simples =================
@ResponseStatus(HttpStatus.NOT_FOUND)
class NotFoundException : RuntimeException("not found")

@ResponseStatus(HttpStatus.FORBIDDEN)
class ForbiddenException : RuntimeException("forbidden")