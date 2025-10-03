package com.softtek.zenite.entity

import org.bson.types.ObjectId
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document
import java.time.Instant

@Document("auth_users")
data class AuthUserDocument(
    @Id val id: ObjectId? = null,
    @Indexed(unique = true) val code: String,
    val passwordHash: String,
    val roles: Set<String> = setOf("USER"),
    val mustChangePassword: Boolean = false,
    val masterPassphraseHash: String? = null,
    @CreatedDate val createdAt: Instant = Instant.now(),
    @LastModifiedDate val updatedAt: Instant = Instant.now()
)