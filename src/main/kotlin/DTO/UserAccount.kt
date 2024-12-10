package mike.dev.DTO

import com.fasterxml.jackson.databind.annotation.JsonSerialize
import kotlinx.serialization.Serializable
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document("account")
@Serializable
data class UserAccount(
    @Id
    val id: String? = null,
    val username: String,
    val email: String,
    val password: String? = null, // Optional, in case you want to support local authentication as well
    val authorities: List<String> = listOf(),
    val googleId: String? = null // For storing Google ID
)