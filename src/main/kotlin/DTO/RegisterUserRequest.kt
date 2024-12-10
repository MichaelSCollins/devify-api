package mike.dev.DTO

data class RegisterUserRequest(
    val name: String,
    val email: String,
    val phone: String,
    val password: String,
    val password2: String,
)

data class RegisterGoogleUserRequest(
    val token: String
)
