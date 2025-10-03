package com.softtek.zenite.service

import com.softtek.zenite.entity.AuthUserDocument
import com.softtek.zenite.repository.AuthUserRepository
import com.softtek.zenite.util.CodeGenerator
import com.softtek.zenite.util.DicewarePassphraseGenerator
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

data class RegisterResult(
    val code: String,
    val masterPassphrase: String
)

@Service
class RegistrationService(
    private val repo: AuthUserRepository,
    private val codeGen: CodeGenerator,
    private val encoder: PasswordEncoder,
    private val passGen: DicewarePassphraseGenerator
) {
    @Transactional
    fun register(rawPassword: String, words: Int? = null): RegisterResult {
        val code = codeGen.newUniqueCode()
        val master = passGen.generate(words)

        val entity = AuthUserDocument(
            code = code,
            passwordHash = encoder.encode(rawPassword),
            masterPassphraseHash = encoder.encode(master)
        )
        repo.save(entity)

        return RegisterResult(code = code, masterPassphrase = master)
    }
}