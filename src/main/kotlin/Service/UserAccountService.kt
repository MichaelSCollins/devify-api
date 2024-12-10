package mike.dev.Service
import mike.dev.jwt.JwtUtil
import org.springframework.stereotype.Service
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.gson.GsonFactory
import mike.dev.DTO.*
import mike.dev.Repository.UserAccountRepository
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import kotlin.jvm.optionals.getOrNull

@Service
class UserAccountService(
    private val userRepository: UserAccountRepository,
    private val jwtUtil: JwtUtil,
) {
    private val passwordEncoder = BCryptPasswordEncoder()
    private val transport = NetHttpTransport()
    private val jsonFactory = GsonFactory.getDefaultInstance()

    fun registerUser(request: RegisterUserRequest): UserAccount {
        val hashedPassword = passwordEncoder.encode(request.password)
        val user = UserAccount(
            username = request.name,
            email = request.email,
            password = hashedPassword,
            authorities = listOf("ROLE_USER")
        )
        return userRepository.save(user)
    }

    fun registerGoogleUser(request: RegisterGoogleUserRequest): UserAccount? {
        val verifier = GoogleIdTokenVerifier.Builder(transport, jsonFactory)
            .setAudience(listOf("YOUR_GOOGLE_CLIENT_ID")) // Replace with your actual Google Client ID
            .build()

        val idToken: GoogleIdToken? = verifier.verify(request.token)
        if (idToken != null) {
            val payload = idToken.payload
            val googleId = payload.subject
            val email = payload.email
            val username = payload["name"] as String

            val existingUser = userRepository.findByGoogleId(googleId)
            if (existingUser != null) {
                return existingUser
            }

            val user = UserAccount(
                username = username,
                email = email,
                googleId = googleId,
                authorities = listOf("ROLE_USER")
            )
            return userRepository.save(user)
        }
        return null
    }

    fun loginUser(request: LoginUserRequest): UserAccount? {
        val user = userRepository.findByEmail(request.email) ?: return null
        return if (passwordEncoder.matches(request.password, user.password)) {
            user
        } else {
            null
        }
    }

    fun decodeJwtToken(token: String): UserAccount? {
        val userId = jwtUtil.extractUserId(token) ?: return null
        val user = findUser(userId)
        return user
    }

    fun findUser(id: String): UserAccount? {
        return userRepository.findById(id).getOrNull();
    }

    fun loginGoogleUser(request: LoginGoogleUserRequest): UserAccount? {
        val verifier = GoogleIdTokenVerifier.Builder(transport, jsonFactory)
            .setAudience(listOf("YOUR_GOOGLE_CLIENT_ID"))
            .build()

        val idToken: GoogleIdToken? = verifier.verify(request.token)
        if (idToken != null) {
            val payload = idToken.payload
            val googleId = payload.subject

            return userRepository.findByGoogleId(googleId)
        }
        return null
    }
}