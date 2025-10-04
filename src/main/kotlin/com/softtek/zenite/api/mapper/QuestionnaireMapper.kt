package com.softtek.zenite.api.mapper

import com.softtek.zenite.api.dto.QuestionData
import com.softtek.zenite.api.dto.QuestionnaireData
import com.softtek.zenite.entity.QuestionEmb
import com.softtek.zenite.entity.QuestionnaireDocument
import org.mapstruct.BeanMapping
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.NullValuePropertyMappingStrategy

@Mapper(componentModel = "spring")
interface QuestionnaireMapper {

    @Mapping(target = "id", expression = "java(doc.getId())")
    fun toDto(doc: QuestionnaireDocument): QuestionnaireData

    fun toDtoList(docs: List<QuestionnaireDocument>): List<QuestionnaireData>

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", expression = "java(q.getId())")
    fun toDto(q: QuestionEmb): QuestionData
}