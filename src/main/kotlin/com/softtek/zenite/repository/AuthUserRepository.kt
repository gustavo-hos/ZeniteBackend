package com.softtek.zenite.repository

import com.softtek.zenite.entity.AuthUserDocument
import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.MongoRepository

interface AuthUserRepository : MongoRepository<AuthUserDocument, ObjectId> {
    fun existsByCode(code: String): Boolean
    fun findByCode(code: String): AuthUserDocument?

    fun deleteByCode(code: String): Long
}