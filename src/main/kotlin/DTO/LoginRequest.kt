package mike.dev.DTO

data class LoginUserRequest(
    val email: String,
    val password: String
)

data class LoginGoogleUserRequest(
    val username: String?,
    val token: String
)