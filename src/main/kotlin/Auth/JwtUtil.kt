package mike.dev.jwt

import io.jsonwebtoken.*
import io.jsonwebtoken.security.Keys
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import mike.dev.Config.JWTConfig
import mike.dev.DTO.UserAccount
import mike.dev.Util.HttpAuthorizationUtil
import org.springframework.stereotype.Component
import java.nio.charset.StandardCharsets
import java.util.Date
import mike.dev.Config.ContentConfig

@Component
class JwtUtil(
    private val contentConfig: ContentConfig,
    private val httpAuthorizationUtil: HttpAuthorizationUtil,
    private val jwtConfig: JWTConfig,
) {
    val userClaim = "user";
    val key = Keys.hmacShaKeyFor(jwtConfig.secretKey.toByteArray(StandardCharsets.UTF_8))
    /**
     * Generates a JWT token with user information embedded in the claims.
     */
    fun generateToken(user: UserAccount): String {
        val date = Date()
        val tokenValidity = jwtConfig.tokenValidity
        val expirationDate = Date(System.currentTimeMillis() + tokenValidity)
        val userJson = encodeUserToJson(user)
        return Jwts.builder()
            .setSubject(user.id)
            .claim(userClaim, userJson)
            .setIssuedAt(date)
            .setExpiration(expirationDate)
            .signWith(key, SignatureAlgorithm.HS256)
            .compact()
    }

    /**
     * Extracts the user ID (subject) from a JWT token.
     */
    fun extractUserId(authorization: String): String? {
        val token = httpAuthorizationUtil.removeBearerTag(authorization)
        return parseClaims(token)?.subject
    }

    /**
     * Extracts the user information as a UserAccount object from the JWT claims.
     */
    fun extractUser(authorization: String): UserAccount? {
        val token = httpAuthorizationUtil.removeBearerTag(authorization)
        return if (isTokenExpired(token)) {
            null
        } else {
            val claims = parseClaims(token)
            val userJson = claims?.get(userClaim, String::class.java)
            if (userJson != null)
                decodeUserFromJson(userJson)
            else null
        }
    }

    /**
     * Checks if the token is expired.
     */
    fun isTokenExpired(token: String): Boolean {
        val expirationDate = parseClaims(token)?.expiration
            ?: return true
        return expirationDate.before(Date()) ?: true
    }

    /**
     * Validates the token by checking user ID and expiration.
     */
    fun validateToken(authorization: String, userId: String): Boolean {
        val token = httpAuthorizationUtil.removeBearerTag(authorization)
        return (extractUserId(token) == userId && !isTokenExpired(token))
    }

    /**
     * Encodes UserAccount object to JSON.
     */
    private fun encodeUserToJson(user: UserAccount): String {
        return try {
            Json.encodeToString(user)
        } catch (e: Exception) {
            throw JwtEncodingException(contentConfig.failedToEncodeUser, e)
        }
    }

    /**
     * Decodes JSON string to UserAccount object.
     */
    private fun decodeUserFromJson(userJson: String): UserAccount? {
        return try {
            Json.decodeFromString(userJson)
        } catch (e: Exception) {
            throw JwtDecodingException(contentConfig.failedToParseUser, e)
        }
    }

    /**
     * Parses and validates JWT token claims.
     */
    private fun parseClaims(authorization: String): Claims? {
        val token = httpAuthorizationUtil.removeBearerTag(authorization)
        val stringifiedUndefined = "undefined"
        if(token.isBlank() || token.equals(stringifiedUndefined)) return null
        return try {
            Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .body
        } catch (e: ExpiredJwtException) {
            return null
        } catch (e: JwtException) {
            val message = e.message ?: contentConfig.failedToParseToken
            throw JwtParsingException(message, cause = e)
        }
    }

    // Custom exceptions for clarity
    class JwtEncodingException(message: String, cause: Throwable) : RuntimeException(message, cause)
    class JwtDecodingException(message: String, cause: Throwable) : RuntimeException(message, cause)
    class JwtParsingException(message: String, cause: Throwable) : RuntimeException(message, cause)
}