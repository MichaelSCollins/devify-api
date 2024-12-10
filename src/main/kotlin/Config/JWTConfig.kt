package mike.dev.Config

import com.google.api.client.util.Value
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

@Component
class JWTConfig {
    @Value("\${jwt.secret}")
    val secretKey: String = "super-secret-passowrd-for-application-thing"
    @Value("\${jwt.tokenValidity}")
    val tokenValidity: Long = 1000 * 60 * 60 // Default: 1 hour
}