package mike.dev.Config

import com.google.api.client.util.Value
import org.springframework.stereotype.Component

@Component
class ContentConfig (
) {
    @Value("content.failed-token-message")
    val failedTokenMessage: String = "Failed to generate JWT token."
    @Value("content.failed-to-parse-user")
    val failedToParseUser: String = "Failed to parse JWT user claim."
    @Value("content.failed-to-parse-token")
    val failedToParseToken: String = "Failed to parse JWT token"
    @Value("content.failed-to-encode-user")
    val failedToEncodeUser: String = "Failed to encode JWT user."
}