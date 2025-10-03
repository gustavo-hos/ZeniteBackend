data class LoginRequest(
    val code: String,
    val password: String
)

data class UserSummary(
    val code: String,
    val roles: List<String> = emptyList()
)

data class LoginResponse(
    val accessToken: String,
    val tokenType: String = "Bearer",
    val expiresIn: Long,
    val refreshToken: String,
    val user: UserSummary
)

data class RefreshRequest(val refreshToken: String)
data class RefreshResponse(
    val accessToken: String,
    val tokenType: String = "Bearer",
    val expiresIn: Long
)

data class RegisterRequest(val password: String, val words: Int? = null)
data class RegisterResponse(val code: String, val masterPassphrase: String)