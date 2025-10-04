package com.softtek.zenite.api.mapper

import com.softtek.zenite.api.dto.MoodData
import com.softtek.zenite.api.dto.MoodRequest
import com.softtek.zenite.entity.MoodDocument
import org.mapstruct.*
import java.time.OffsetDateTime

@Mapper(
    componentModel = "spring",
    unmappedTargetPolicy = ReportingPolicy.IGNORE,
    unmappedSourcePolicy = ReportingPolicy.IGNORE
)
abstract class MoodMapper {

    @Mappings(
        Mapping(target = "id", ignore = true),
        Mapping(target = "createdAt", ignore = true)
    )
    abstract fun toDocument(rq: MoodRequest): MoodDocument

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mappings(
        Mapping(target = "id", ignore = true),
        Mapping(target = "code", ignore = true),
        Mapping(target = "createdAt", ignore = true)
    )
    abstract fun updateDocument(@MappingTarget target: MoodDocument, rq: MoodRequest)

    abstract fun toData(doc: MoodDocument): MoodData
    abstract fun toDataList(docs: List<MoodDocument>): List<MoodData>

    @AfterMapping
    protected fun setDefaults(@MappingTarget target: MoodDocument) {
        if (target.createdAt == null) {
            target.createdAt = OffsetDateTime.now()
        }
    }
}
