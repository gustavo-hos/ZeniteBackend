package com.softtek.zenite.repository

import com.softtek.zenite.entity.QuestionnaireDocument
import org.springframework.data.mongodb.repository.MongoRepository

interface QuestionnaireRepository : MongoRepository<QuestionnaireDocument, String>
