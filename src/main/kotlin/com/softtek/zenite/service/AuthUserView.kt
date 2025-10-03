package com.softtek.zenite.service

import com.softtek.zenite.entity.AuthUserDocument
import com.softtek.zenite.repository.AuthUserRepository
import com.softtek.zenite.util.CodeGenerator
import com.softtek.zenite.util.DicewarePassphraseGenerator
import org.springframework.dao.DuplicateKeyException
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

data class AuthUserView(
    val code: String,
    val roles: Set<String>,
    val mustChangePassword: Boolean
)

data class CreateUserCommand(
    val code: String? = null,
    val password: String? = null,
    val roles: Set<String> = setOf("USER"),
    val dicewareWords: Int? = null
)

data class CreateUserResult(
    val code: String,
    val temporaryPassphrase: String?
)

data class UpdateRolesCommand(val roles: Set<String>)
data class AdminResetPasswordCommand(val newPassword: String? = null, val dicewareWords: Int? = 4)
data class ChangeOwnPasswordCommand(val currentPassword: String, val newPassword: String)

@Service
class AuthUserService(
    private val repo: AuthUserRepository,
    private val encoder: PasswordEncoder,
    private val codeGen: CodeGenerator,
    private val diceware: DicewarePassphraseGenerator
) {
    fun list(page: Int, size: Int): Page<AuthUserView> =
        repo.findAll(PageRequest.of(page, size))
            .map { it.toView() }

    fun get(code: String): AuthUserView =
        repo.findByCode(code)?.toView() ?: notFound(code)

    fun create(cmd: CreateUserCommand): CreateUserResult {
        val code = cmd.code?.trim().takeUnless { it.isNullOrBlank() } ?: codeGen.newUniqueCode()
        if (repo.existsByCode(code)) throw DuplicateKeyException("User code already exists: $code")

        val (passToHash, tempShown) =
            if (cmd.password.isNullOrBlank()) {
                val temp = diceware.generate(cmd.dicewareWords ?: 4)
                temp to temp
            } else cmd.password to null

        val entity = AuthUserDocument(
            code = code,
            passwordHash = encoder.encode(passToHash),
            roles = cmd.roles.ifEmpty { setOf("USER") },
            mustChangePassword = tempShown != null
        )
        repo.save(entity)
        return CreateUserResult(code = code, temporaryPassphrase = tempShown)
    }

    fun updateRoles(code: String, cmd: UpdateRolesCommand): AuthUserView {
        val u = repo.findByCode(code) ?: notFound(code)
        val updated = u.copy(roles = cmd.roles.ifEmpty { setOf("USER") })
        return repo.save(updated).toView()
    }

    fun adminResetPassword(code: String, cmd: AdminResetPasswordCommand): String {
        val u = repo.findByCode(code) ?: notFound(code)
        val newPlain =
            cmd.newPassword?.takeIf { it.isNotBlank() } ?: diceware.generate(cmd.dicewareWords ?: 4)
        val updated = u.copy(
            passwordHash = encoder.encode(newPlain),
            mustChangePassword = cmd.newPassword.isNullOrBlank()
        )
        repo.save(updated)
        return newPlain // se foi Diceware, retorne pro admin exibir uma única vez
    }

    fun changeOwnPassword(code: String, cmd: ChangeOwnPasswordCommand) {
        val u = repo.findByCode(code) ?: notFound(code)
        require(encoder.matches(cmd.currentPassword, u.passwordHash)) { "Invalid current password" }
        val updated = u.copy(
            passwordHash = encoder.encode(cmd.newPassword),
            mustChangePassword = false
        )
        repo.save(updated)
    }

    fun delete(code: String) {
        val u = repo.findByCode(code) ?: notFound(code)
        repo.delete(u)
    }

    private fun AuthUserDocument.toView() =
        AuthUserView(code = code, roles = roles, mustChangePassword = mustChangePassword)

    private fun notFound(code: String): Nothing =
        throw NoSuchElementException("AuthUser not found: $code")
}