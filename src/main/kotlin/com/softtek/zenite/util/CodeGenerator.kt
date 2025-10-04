package com.softtek.zenite.util

import com.softtek.zenite.repository.AuthUserRepository
import org.springframework.stereotype.Component
import java.security.SecureRandom

@Component
class CodeGenerator(private val repo: AuthUserRepository) {
    private val rng = SecureRandom()
    private val alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789"

    fun newUniqueCode(length: Int = 5, maxTries: Int = 10000): String {
        repeat(maxTries) {
            val code = buildString(length) {
                repeat(length) { append(alphabet[rng.nextInt(alphabet.length)]) }
            }
            if (!repo.existsByCode(code)) return code
        }
        throw IllegalStateException("Não foi possível gerar um código único após $maxTries tentativas.")
    }
}
